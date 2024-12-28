package com.example.backend.controller;

import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/drivers/spot")
public class ParkingSpotController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/reserve")
    public ResponseEntity<Object> reserveSpot(@RequestBody Map<String, Integer> request) {
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        int duration = request.get("duration");
        try {
            driverService.reserveSpot(parkingSpotId, parkingLotId, duration);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/arrive")
    public ResponseEntity<Object> arrival(@RequestBody Map<String, Integer> request) {
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        try {
            driverService.driverArrival(parkingSpotId, parkingLotId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/leave")
    public ResponseEntity<Object> leave(@RequestBody Map<String, Integer> request) {
        int parkingSpotId = request.get("parkingSpotId");
        int parkingLotId = request.get("parkingLotId");
        try {
            // TODO: LW m4a ems7 el row mn el DB reservation table
            driverService.driverDeparture(parkingSpotId, parkingLotId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getCurrentReservedSpots() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(driverService.getReservedSpots());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
