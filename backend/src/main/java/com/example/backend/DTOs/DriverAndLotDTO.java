package com.example.backend.DTOs;

public class DriverAndLotDTO {
    private int driverId;
    private int timeDiff ;
    private int penalty;
    private int price ;
    private int parkingLotId;
    private int parkingSpotId;

    public DriverAndLotDTO(int driverId, int timeDiff, int penalty, int price, int parkingLotId, int parkingSpotId) {
        this.driverId = driverId;
        this.timeDiff = timeDiff;
        this.penalty = penalty;
        this.price = price;
        this.parkingLotId = parkingLotId;
        this.parkingSpotId = parkingSpotId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(int timeDiff) {
        this.timeDiff = timeDiff;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }
}
