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
    private Timestamp arrivalTime;
    private Timestamp leaveTime;
    private int driverId;
    private int parkingSpotParkingLotId;
    private int parkingSpotId;
    private double price;
    private double penalty;

    public ReservedSpot(Timestamp endTime, Timestamp startTime, int driverId, int parkingSpotParkingLotId, int parkingSpotId) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.driverId = driverId;
        this.parkingSpotParkingLotId = parkingSpotParkingLotId;
        this.parkingSpotId = parkingSpotId;
    }
}


// CREATE TABLE IF NOT EXISTS `parkdb`.`Reserved_Spot` (
//  `end_time` TIMESTAMP(3) NOT NULL,
//  `start_time` TIMESTAMP(3) NOT NULL,
//  `arrival_time` TIMESTAMP(3) NULL,
//  `leave_time` TIMESTAMP(3) NULL,
//  `Driver_id` INT NOT NULL,
//  `Parking_Spot_Parking_Lot_id` INT NOT NULL,
//  `Parking_Spot_id` INT NOT NULL,
//  `price` DECIMAL NOT NULL,
//  `penalty` DECIMAL NULL,
//  PRIMARY KEY (`Driver_id`, `Parking_Spot_Parking_Lot_id`, `Parking_Spot_id`, `start_time`),
//  INDEX `fk_Reserved_Spot_Driver1_idx` (`Driver_id` ASC) VISIBLE,
//  INDEX `fk_Reserved_Spot_Parking_Spot1_idx` (`Parking_Spot_Parking_Lot_id` ASC, `Parking_Spot_id` ASC) VISIBLE,
//  CONSTRAINT `fk_Reserved_Spot_Driver1`
//    FOREIGN KEY (`Driver_id`)
//    REFERENCES `parkdb`.`Driver` (`id`)
//    ON DELETE CASCADE
//    ON UPDATE CASCADE,
//  CONSTRAINT `fk_Reserved_Spot_Parking_Spot1`
//    FOREIGN KEY (`Parking_Spot_Parking_Lot_id` , `Parking_Spot_id`)
//    REFERENCES `parkdb`.`Parking_Spot` (`Parking_Lot_id` , `id`)
//    ON DELETE CASCADE
//    ON UPDATE CASCADE)
//ENGINE = InnoDB;