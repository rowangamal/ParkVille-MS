package com.example.backend.DTOs;

public class DriverNotificationDTO {
    private int driverId;
    private int timeDiff ;
    private int penalty;
    

    public DriverNotificationDTO(int driverId, int timeDiff, int penalty) {
        this.driverId = driverId;
        this.timeDiff = timeDiff;
        this.penalty = penalty;
    }
    public DriverNotificationDTO(int driverId, int timeDiff) {
        this.driverId = driverId;
        this.timeDiff = timeDiff;
    }
    public DriverNotificationDTO() {
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
}
