package com.example.backend.controller;

import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/spot")
public class SpotController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/reserve")
    public ResponseEntity<Object> reserveSpot(@RequestBody Map<String, Integer> request) {
        int driverId = request.get("driverId");
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        int duration = request.get("duration");
        try {
            driverService.reserveSpot(driverId, parkingSpotId, parkingLotId, duration);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/arrival")
    public ResponseEntity<Object> arrival(@RequestBody Map<String, Integer> request) {
        int driverId = request.get("driverId");
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        try {
            driverService.driverArrival(driverId, parkingSpotId, parkingLotId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
