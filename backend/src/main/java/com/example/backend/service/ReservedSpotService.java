package com.example.backend.service;

import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ReservedSpotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservedSpotService {
    @Autowired
    private ReservedSpotRepo reservedSpotRepo;

    public ReservedSpot getReservedSpot(int driverId, int parkingSpotId, int parkingLotId){
        return reservedSpotRepo.getReservedSpot(driverId, parkingSpotId, parkingLotId);
    }
}
