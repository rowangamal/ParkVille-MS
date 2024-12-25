package com.example.backend.repository;

import com.example.backend.model.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ParkingSpotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    private void updateStatus(int id, String status){
        String sqlStatement = "update Parking_Spot set status = ? where id = ?";
        jdbcTemplate.update(sqlStatement, status, id);
    }


}
