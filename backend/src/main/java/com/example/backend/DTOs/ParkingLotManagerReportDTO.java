package com.example.backend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotManagerReportDTO {
    private int parkingLotId;
    private String longitude;
    private String latitude;
    private int capacity;
    private int totalSpots;
    private int occupiedSpots;
    private int availableSpots;
    private double totalRevenue;
    private double totalPenalties;
    private double occupancyRate;
    private double OverstayPenalties;
}