package com.example.backend.service;

import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.Driver;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SpotOccupationTimeService spotOccupationTimeService;


    @Autowired
    private JWTService jwtService;

    public String signup(Driver driver) {
        List<Driver> existingDrivers = driverRepo.getAll();
        Optional<Driver> existingDriver = existingDrivers.stream()
                .filter(d -> d.getUsername().equals(driver.getUsername()))
                .findFirst();

        if (existingDriver.isPresent()) {
            return "Username already exists.";
        }

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
        if (!driver.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password.");
        }
        String jwt = jwtService.generateToken(driver.getId(), driver.getRole());
        return new SuccessLoginDTO(driver.getId(), driver.getUsername(), driver.getRole(), jwt);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reserveSpot(int driverId, int parkingSpotId, int parkingLotId, int duration){
        parkingSpotService.reserveSpot(parkingSpotId, parkingLotId);
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + (long) duration *1000*60*60); // duration in hours
        parkingSpotService.createReservation(driverId, parkingSpotId, parkingLotId, startTime, endTime);

    }

    @Transactional
    public void driverArrival(int driverId, int parkingSpotId, int parkingLotId){
        parkingSpotService.updateSpotStatus(parkingSpotId, parkingLotId, "occupied");
        ReservedSpot reservedSpot = reservedSpotService.getReservedSpot(driverId, parkingSpotId, parkingLotId);
        spotOccupationTimeService.driverArrival(reservedSpot);
    }

    @Transactional
    public void driverDeparture(int driverId, int parkingSpotId, int parkingLotId){
        ReservedSpot reservedSpot = reservedSpotService.getReservedSpot(driverId, parkingSpotId, parkingLotId);
        spotOccupationTimeService.driverDeparture(reservedSpot);
        parkingSpotService.updateSpotStatus(parkingSpotId, parkingLotId, "empty");
//        reservedSpotService.delete(driverId, parkingSpotId, parkingLotId);
    }

}
