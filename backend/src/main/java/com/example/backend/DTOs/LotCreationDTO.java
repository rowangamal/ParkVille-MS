package com.example.backend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotCreationDTO {
    @Override
    public String toString() {
        return "LotCreationDTO{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", pricePerHour=" + pricePerHour +
                ", numberOfSlots=" + numberOfSlots +
                ", parkingType='" + parkingType + '\'' +
                '}';
    }

    private String longitude;
    private String  latitude;
    private double pricePerHour;
    private int numberOfSlots;
    private String parkingType;
}
