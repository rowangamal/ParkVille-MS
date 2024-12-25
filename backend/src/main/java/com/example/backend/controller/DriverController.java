package com.example.backend.controller;

import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.model.Driver;
import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/signup")
    public String signup(@RequestBody Driver driver) {
        System.out.println("i am driver");
        return driverService.signup(driver);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        String message = driverService.login(loginRequest.getUsername(), loginRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        HttpStatus status = message.equals("Login successful.") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/reserve/spot")
    public ResponseEntity<Object> reserveSpot(@RequestBody Map<String, Integer> request) {
        int driverId = request.get("driverId");
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        int duration = request.get("duration");
        System.out.println("spot id is " + parkingSpotId);
        System.out.println("lot id is " + parkingLotId);

        try {
            driverService.reserveSpot(driverId, parkingSpotId, parkingLotId, duration);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
