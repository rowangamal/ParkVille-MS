package com.example.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PenaltyRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

}
//
//`intial_penalty` DECIMAL NOT NULL,
//        `id` INT NOT NULL,
//        `Reserved_Spot_Driver_id` INT NOT NULL,
//        `Reserved_Spot_Parking_Spot_Parking_Lot_id` INT NOT NULL,
//        `Reserved_Spot_Parking_Spot_id` INT NOT NULL,