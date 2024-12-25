package com.example.backend.service;

import com.example.backend.exception.SpotReservedException;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ParkingSpotRepo;
import com.example.backend.repository.ReservedSpotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ParkingSpotService {
    @Autowired
    private ParkingSpotRepo parkingSpotRepo;

    @Autowired
    private ReservedSpotRepo reservedSpotRepo;

    public void reserveSpot(int id, int parkingLotId){
        String getSpotStatus = parkingSpotRepo.getstatus(id, parkingLotId);
        if (getSpotStatus.equals("reserved")){
            throw new SpotReservedException("Spot is already reserved.");
        }
        parkingSpotRepo.updateStatus(id, parkingLotId, "reserved");
    }

    public String getSpotStatus(int spotId, int parkingLotId){
        return parkingSpotRepo.getstatus(spotId, parkingLotId);
    }

    public void updateSpotStatus(int id, int parkingLotId, String status){
        parkingSpotRepo.updateStatus(id, parkingLotId, status);
    }

    public void createReservation(int driverId, int parkingSpotId, int parkingLotId, Timestamp startTime, Timestamp endTime){
        ReservedSpot reservedSpot = new ReservedSpot(startTime, endTime, driverId, parkingLotId, parkingSpotId);
        reservedSpotRepo.save(reservedSpot);
    }

}
