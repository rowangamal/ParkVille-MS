package com.example.backend.controller;

import com.example.backend.model.ParkingLot;
import com.example.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
//
//    @GetMapping("/lot-revenues")
//    public ResponseEntity<List<ParkingLot>> getParkingLotRevenues(){
//        List<ParkingLot> revenues = dashboardService.getParkingLotRevenues();
//        return ResponseEntity.ok(revenues);
//    }
//
//    @PostMapping("/lot/occupancy-rate")
//    public ResponseEntity<Double> getOccupancyRate(@RequestBody ParkingLot parkingLot) {
//        double occupancyRate = dashboardService.getOccupancyRate(parkingLot.getId());
//        return ResponseEntity.ok(occupancyRate);
//    }
//
//    @PostMapping("/lot/total-revenue")
//    public ResponseEntity<Double> getTotalRevenue(@RequestBody ParkingLot parkingLot) {
//        double totalRevenue = dashboardService.getTotalRevenue(parkingLot.getId());
//        return ResponseEntity.ok(totalRevenue);
//    }
//
//    @PostMapping("/lot/total-penalties")
//    public ResponseEntity<Double> getTotalPenalties(@RequestBody ParkingLot parkingLot) {
//        double totalPenalties = dashboardService.getTotalPenalties(parkingLot.getId());
//        return ResponseEntity.ok(totalPenalties);
//    }
//
//    @GetMapping("/top-users")
//    public ResponseEntity<List<Map<String, Object>>> getTopUsers() {
//        List<Map<String, Object>> topUsers = dashboardService.getTopUsers();
//        return ResponseEntity.ok(topUsers);
//    }
}
