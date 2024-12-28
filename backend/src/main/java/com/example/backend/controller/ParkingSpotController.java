package com.example.backend.controller;

import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<Object> arrival(@RequestBody Map<String, String> request) {
        int parkingSpotId = Integer.parseInt(request.get("parkingSpotId"));
        int parkingLotId = Integer.parseInt(request.get("parkingLotId"));
        String startTimeString = request.get("startTime");
        ZonedDateTime utcDateTime = ZonedDateTime.parse(startTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime gmtPlus2DateTime = utcDateTime.withZoneSameInstant(ZoneId.of("GMT+2"));
        Timestamp startTime = Timestamp.valueOf(gmtPlus2DateTime.toLocalDateTime());
        System.out.println(startTime);
//        System.out.println();
        try {
            driverService.driverArrival(parkingSpotId, parkingLotId , startTime);
            System.out.println("Arrived");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/leave")
    public ResponseEntity<Object> leave(@RequestBody Map<String, String> request) {
        int parkingSpotId = Integer.parseInt(request.get("parkingSpotId"));
        int parkingLotId = Integer.parseInt(request.get("parkingLotId"));
        String startTimeString = request.get("startTime");
        ZonedDateTime utcDateTime = ZonedDateTime.parse(startTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime gmtPlus2DateTime = utcDateTime.withZoneSameInstant(ZoneId.of("GMT+2"));

        Timestamp startTime = Timestamp.valueOf(gmtPlus2DateTime.toLocalDateTime());
        System.out.println(startTime);

        try {
            // TODO: LW m4a ems7 el row mn el DB reservation table
            driverService.driverDeparture(parkingSpotId, parkingLotId , startTime);
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
