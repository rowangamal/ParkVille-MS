package com.example.backend.service;

import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ReservedSpotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReservedSpotService {
    @Autowired
    private ReservedSpotRepo reservedSpotRepo;

    public ReservedSpot getReservedSpot(int driverId, int parkingSpotId, int parkingLotId , Timestamp startTime){
        return reservedSpotRepo.getReservedSpot(driverId, parkingSpotId, parkingLotId , startTime);
    }

    public void delete(int driverId, int parkingSpotId, int parkingLotId){
        reservedSpotRepo.delete(driverId, parkingSpotId, parkingLotId);
    }

    public void createReservation(ReservedSpot reservedSpot){
        reservedSpotRepo.save(reservedSpot);
    }

    public void updateArrivalTime(ReservedSpot reservedSpot){
        reservedSpot.setArrivalTime(new Timestamp(System.currentTimeMillis()));
        reservedSpotRepo.updateArrivalTime(reservedSpot);
    }

    public void updateLeaveTime(ReservedSpot reservedSpot){
        reservedSpot.setLeaveTime(new Timestamp(System.currentTimeMillis()));
        reservedSpotRepo.updateLeaveTime(reservedSpot);
    }

    public List<ReservedSpot> getDriverReservedSpots(int driverId){
        return reservedSpotRepo.getDriverReservedSpots(driverId);
    }



}
