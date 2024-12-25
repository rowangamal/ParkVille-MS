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
public class ReservedSpot {
    private Timestamp endTime;
    private Timestamp startTime;
    private int driverId;
    private int parkingSpotParkingLotId;
    private int parkingSpotId;
}


