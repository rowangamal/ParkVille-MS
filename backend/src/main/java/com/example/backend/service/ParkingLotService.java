package com.example.backend.service;

import com.example.backend.DTOs.LotLocationDTO;
import com.example.backend.DTOs.ParkingLotResponseDTO;
import com.example.backend.model.ParkingLot;
import com.example.backend.model.ReservedSpot;
import com.example.backend.repository.ParkingLotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepo parkingLotRepo;

    public ParkingLot getParkingLotById(int id){
        return parkingLotRepo.getParkingLotById(id);
    }
    public Boolean checkParkingLotExists(String latitude, String longitude) {
        return parkingLotRepo.isParkingLotFound(latitude, longitude);
    }

    public ParkingLotResponseDTO getParkingLotInfo(String latitude, String longitude) {
        return parkingLotRepo.getParkingLotInfo(latitude, longitude);
    }

    public List<LotLocationDTO> getAllParkingLots() {
        return parkingLotRepo.getAllParkingLots();
    }
}
