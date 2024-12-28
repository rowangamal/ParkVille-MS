package com.example.backend.service;

import com.example.backend.exception.SpotReservedException;
import com.example.backend.model.ParkingLot;
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
    private ReservedSpotService reservedSpotService;

    @Autowired
    private ParkingLotService parkingLotService;

    public void reserveSpot(int id, int parkingLotId){
        String getSpotStatus = getSpotStatus(id, parkingLotId);
        if (!getSpotStatus.equals("empty")){
            throw new SpotReservedException("Spot is already reserved.");
        }
        parkingSpotRepo.updateStatus(id, parkingLotId, "reserved");
    }

    public String getSpotStatus(int spotId, int parkingLotId){
        return parkingSpotRepo.getstatus(spotId, parkingLotId);
    }

    public void updateSpotStatus(int id, int parkingLotId ,  String status){
        parkingSpotRepo.updateStatus(id, parkingLotId, status);
    }

    public void createReservation(ReservedSpot reservedSpot){
        reservedSpotService.createReservation(reservedSpot);
    }

    public int countEmptySpots(int parkingLotId){
        return parkingSpotRepo.countEmptySpots(parkingLotId);
    }

    public int countAllSpots(int parkingLotId){
        return parkingSpotRepo.countAllSpots(parkingLotId);
    }

    public ReservedSpot dynamicPricing(ReservedSpot reservedSpot){
        ParkingLot parkingLot = parkingLotService.getParkingLotById(reservedSpot.getParkingSpotParkingLotId());
        double initialPrice = parkingLot.getPrice(); // per hour

        Timestamp startTime = reservedSpot.getStartTime();
        Timestamp endTime = reservedSpot.getEndTime();
        int duration = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60));
        initialPrice *= duration;

        int emptySpots = countEmptySpots(reservedSpot.getParkingSpotParkingLotId());
//        int allSpots = parkingSpotService.countAllSpots(reservedSpot.getParkingSpotParkingLotId());
        //by capacity we're saving redundant query
        initialPrice = initialPrice * (1 + (double) (parkingLot.getCapacity() - emptySpots) / parkingLot.getCapacity());

        // peak hour calculation
        if (Utility.getHourOfTimeStamp(startTime) >= 14 && Utility.getHourOfTimeStamp(startTime) <= 17){
            initialPrice *= 1.5;
        }

        reservedSpot.setPrice(initialPrice);
        return reservedSpot;
    }


}
