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
@Getter
@Setter
public class ParkingLot {
    private int id;
    private String longitude;
    private String latitude;
    private int capacity;
    private double price;
    private String type;
    private double revenue;
    private int ParkingLotManagerid;
}
