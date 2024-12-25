package com.example.backend.service;

import com.example.backend.model.ReservedSpot;
import com.example.backend.model.SpotOccupationTime;
import com.example.backend.repository.SpotOccupationTimeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SpotOccupationTimeService {
    @Autowired
    private SpotOccupationTimeRepo spotOccupationTimeRepo;

    public void driverArrival(ReservedSpot reservedSpot) {
        SpotOccupationTime spotOccupationTime = new SpotOccupationTime(reservedSpot.getDriverId(), reservedSpot.getParkingSpotParkingLotId(),
                reservedSpot.getParkingSpotId(), new Timestamp(System.currentTimeMillis()));
        spotOccupationTimeRepo.save(spotOccupationTime);
    }
}
