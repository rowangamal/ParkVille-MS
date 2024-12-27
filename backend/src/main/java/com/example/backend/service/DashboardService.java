package com.example.backend.service;

import com.example.backend.model.ParkingLot;
import com.example.backend.repository.DashboardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepo dashboardRepo;
//
//
//    public List<ParkingLot> getParkingLotRevenues() {
//        return dashboardRepo.getTopParkingLotRevenues();
//    }
//
//    public List<Map<String, Object>> getTopUsers() {
//        return dashboardRepo.getTopUsers();
//    }
//
//    public double getOccupancyRate(int parkingLotId) {
//        return dashboardRepo.getOccupancyRate(parkingLotId);
//    }
//
//    public double getTotalRevenue(int parkingLotId) {
//        return dashboardRepo.getTotalRevenue(parkingLotId);
//    }
//
//    public double getTotalPenalties(int parkingLotId) {
//        return dashboardRepo.getTotalPenalties(parkingLotId);
//    }
}