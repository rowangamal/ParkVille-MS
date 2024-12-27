package com.example.backend.service;

import com.example.backend.DTOs.ParkingLotResponseDTO;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ParkingLotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepo parkingLotRepo;
    @Autowired
    private ParkingSpotService parkingSpotService;
    @Autowired
    private ReservedSpotService reservedSpotService;

    public ReservedSpot dynamicPricing(ReservedSpot reservedSpot){
        double initialPrice = 20.0; // per hour
        Timestamp startTime = reservedSpot.getStartTime();
        Timestamp endTime = reservedSpot.getEndTime();
        int duration = (int) ((endTime.getTime() - startTime.getTime()) / 60*1000); // price by mins
        int emptySpots = parkingSpotService.countEmptySpots(reservedSpot.getParkingSpotParkingLotId());
        int allSpots = parkingSpotService.countAllSpots(reservedSpot.getParkingSpotParkingLotId());
        initialPrice *= duration;
        initialPrice = initialPrice * (1 + (double) (allSpots - emptySpots) / allSpots);
        // peak hour calculation
        if (Utility.getHourOfTimeStamp(startTime) >= 14 && Utility.getHourOfTimeStamp(startTime) <= 17){
            initialPrice *= 1.5;
        }
        reservedSpot.setPrice(initialPrice);
        return reservedSpot;
    }

    public Boolean checkParkingLotExists(String latitude, String longitude) {
        return parkingLotRepo.isParkingLotFound(latitude, longitude);
    }

    public ParkingLotResponseDTO getParkingLotInfo(String latitude, String longitude) {
        return parkingLotRepo.getParkingLotInfo(latitude, longitude);
    }
}
