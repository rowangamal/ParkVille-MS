package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SpotOccupationTime {
    private int ReservedSpotDriverId;
    private int ReservedSpotParkingSpotParkingLotId;
    private int ReservedSpotParkingSpotId;
    private Timestamp arrivalTime;
    private Timestamp leaveTime;

    public SpotOccupationTime(int reservedSpotDriverId, int reservedSpotParkingSpotParkingLotId,
                              int reservedSpotParkingSpotId, Timestamp arrivalTime) {
        ReservedSpotDriverId = reservedSpotDriverId;
        ReservedSpotParkingSpotParkingLotId = reservedSpotParkingSpotParkingLotId;
        ReservedSpotParkingSpotId = reservedSpotParkingSpotId;
        this.arrivalTime = arrivalTime;
    }
}

