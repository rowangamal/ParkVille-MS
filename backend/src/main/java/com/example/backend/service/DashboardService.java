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

    // Get parking lot revenues
    public List<ParkingLot> getParkingLotRevenues() {
        return dashboardRepo.getParkingLotRevenues();
    }

    // Get occupancy rate for a specific parking lot
    public double getOccupancyRate(int parkingLotId) {
        return dashboardRepo.getOccupancyRate(parkingLotId);
    }

    // Get total revenue for a specific parking lot
    public double getTotalRevenue(int parkingLotId) {
        return dashboardRepo.getTotalRevenue(parkingLotId);
    }

    // Get total penalties for a specific parking lot
    public double getTotalPenalties(int parkingLotId) {
        return dashboardRepo.getTotalPenalties(parkingLotId);
    }

    // Get top users (Drivers with most reservations)
    public List<Map<String, Object>> getTopUsers() {
        return dashboardRepo.getTopUsers();
    }
}
