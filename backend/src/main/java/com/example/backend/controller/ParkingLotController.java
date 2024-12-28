package com.example.backend.controller;

import com.example.backend.DTOs.LotLocationDTO;
import com.example.backend.DTOs.ParkingLotDTO;
import com.example.backend.DTOs.ParkingLotResponseDTO;
import com.example.backend.model.ParkingLot;
import com.example.backend.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @PostMapping("/is-parking-lot-created")
    public ResponseEntity<?> checkParkingLot(@RequestBody ParkingLotDTO parkingLot) {
        Boolean isParkingLotFound = parkingLotService.checkParkingLotExists(parkingLot.getLatitude(), parkingLot.getLongitude());

        if (isParkingLotFound) {
            ParkingLotResponseDTO parkingLotResponseDTO = parkingLotService.getParkingLotInfo(parkingLot.getLatitude(), parkingLot.getLongitude());
            return ResponseEntity.ok(parkingLotResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking lot not found");
        }
    }

    @GetMapping("/all-lots")
    public ResponseEntity<Object> getAllParkingLots() {
        try {
            List<LotLocationDTO> parkingLots = parkingLotService.getAllParkingLots();
            return ResponseEntity.ok(parkingLots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }}
