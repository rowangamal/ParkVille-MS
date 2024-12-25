package com.example.backend.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Penalty {
    private double initialPenalty;
    private int id;
    private int ReservedSpotDriverId;
    private int ReservedSpotParkingSpotParkingLotId;
    private int ReservedSpotParkingSpotId;
}
