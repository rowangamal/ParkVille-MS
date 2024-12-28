package com.example.backend.DTOs;

import com.example.backend.model.ParkingSpot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParkingLotResponseDTO {
    private int id;
    private String longitude;
    private String latitude;
    private double pricePerHour;
    private String parkingType;
    private List<ParkingSpot> parkingSpots;
}
