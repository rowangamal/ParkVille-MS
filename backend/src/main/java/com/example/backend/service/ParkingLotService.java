package com.example.backend.service;

import com.example.backend.model.ParkingLot;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ParkingLotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepo parkingLotRepo;

    public ParkingLot getParkingLotById(int id){
        return parkingLotRepo.getParkingLotById(id);
    }

}
