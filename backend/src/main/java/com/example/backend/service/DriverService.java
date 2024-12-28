package com.example.backend.service;

import com.example.backend.DTOs.NotificationMessageDTO;
import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.CustomUserDetails;
import com.example.backend.model.Driver;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    private ParkingSpotService parkingSpotService;
    @Autowired
    private ReservedSpotService reservedSpotService;


    @Autowired
    private JWTService jwtService;

    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    public DriverService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String signup(Driver driver) {
        List<Driver> existingDrivers = driverRepo.getAll();
        Optional<Driver> existingDriver = existingDrivers.stream()
                .filter(d -> d.getUsername().equals(driver.getUsername()))
                .findFirst();

        if (existingDriver.isPresent()) {
            return "Username already exists.";
        }

        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        driver.setPassword(encoder.encode(driver.getPassword()));

        driverRepo.save(driver);
        return "Signup successful.";
    }

    public SuccessLoginDTO login(String username, String password) {
        List<Driver> existingDrivers = driverRepo.getAll();
        Optional<Driver> matchingDriver = existingDrivers.stream()
                .filter(d -> d.getUsername().equals(username))
                .findFirst();

        System.out.println(matchingDriver);
        if (matchingDriver.isEmpty()) {
            throw  new RuntimeException("Invalid email or password.");
        }

        Driver driver = matchingDriver.get();
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if (!encoder.matches(password, driver.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }
        String jwt = jwtService.generateToken(driver.getId(), driver.getRole());
        return new SuccessLoginDTO(driver.getId(), driver.getUsername(), driver.getRole(), jwt);
    }

    public int getDriverId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated or invalid principal");
        }
        return ((CustomUserDetails)authentication.getPrincipal()).getUserId();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reserveSpot(int parkingSpotId, int parkingLotId, int duration){

        parkingSpotService.reserveSpot(parkingSpotId, parkingLotId);
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + (long) duration *1000*60*60); // duration in hours
        ReservedSpot reservedSpot = parkingSpotService.dynamicPricing(new ReservedSpot(endTime, startTime, this.getDriverId(), parkingLotId, parkingSpotId));
        parkingSpotService.createReservation(reservedSpot);

    }

    @Transactional
    public void driverArrival(int parkingSpotId, int parkingLotId , Timestamp startTime){
        parkingSpotService.updateSpotStatus(parkingSpotId, parkingLotId , "occupied");
        ReservedSpot reservedSpot = reservedSpotService.getReservedSpot(this.getDriverId(), parkingSpotId, parkingLotId , startTime);
        reservedSpotService.updateArrivalTime(reservedSpot);
        messagingTemplate.convertAndSend("/topic/notifications/lotArrive/"+parkingLotId, new NotificationMessageDTO(parkingSpotId +""));

    }

    @Transactional
    public void driverDeparture(int parkingSpotId, int parkingLotId , Timestamp startTime){
        ReservedSpot reservedSpot = reservedSpotService.getReservedSpot(this.getDriverId(), parkingSpotId, parkingLotId , startTime);
        reservedSpotService.updateLeaveTime(reservedSpot);
        parkingSpotService.updateSpotStatus(parkingSpotId, parkingLotId, "empty");

        messagingTemplate.convertAndSend("/topic/notifications/lot/"+parkingLotId, new NotificationMessageDTO(parkingSpotId +""));
    }

    public List<ReservedSpot> getReservedSpots(){
        return reservedSpotService.getDriverReservedSpots(this.getDriverId());
    }

}
