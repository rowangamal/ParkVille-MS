package com.example.backend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotManagerReportDTO {
    private int parkingLotId;
    private double occupancyRate;
    private double reservedRate;
    private double freeRate;
    private double totalRevenue;
    private double totalViolations;
}