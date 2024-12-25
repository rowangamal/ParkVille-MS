package com.example.backend.service;

import com.example.backend.model.Driver;
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

    public String login(String username, String password) {
        List<Driver> existingDrivers = driverRepo.getAll();
        Optional<Driver> matchingDriver = existingDrivers.stream()
                .filter(d -> d.getUsername().equals(username))
                .findFirst();

        if (matchingDriver.isEmpty()) {
            return "Invalid username.";
        }

        Driver driver = matchingDriver.get();
        if (!driver.getPassword().equals(password)) {
            return "Invalid password.";
        }
        return "Login successful.";
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reserveSpot(int driverId, int parkingSpotId, int parkingLotId, int duration){
        parkingSpotService.reserveSpot(parkingSpotId, parkingLotId);
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + (long) duration *1000*60*60); // duration in hours
        parkingSpotService.createReservation(driverId, parkingSpotId, parkingLotId, startTime, endTime);

    }
}
