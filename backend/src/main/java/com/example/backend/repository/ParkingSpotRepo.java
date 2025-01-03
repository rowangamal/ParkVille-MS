package com.example.backend.repository;

import com.example.backend.model.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class ParkingSpotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void updateStatus(int id, int parkingLotId ,  String status){
        String sqlStatement = "update Parking_Spot set status = ? where id = ? and Parking_Lot_id = ? ";
        jdbcTemplate.update(sqlStatement, status, id, parkingLotId );
    }

    public String getstatus(int id, int parkingLotId){
        String sqlStatement = "select status from Parking_Spot where id = ? and Parking_Lot_id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, String.class, id, parkingLotId);
    }

    public int countEmptySpots(int parkingLotId){
        String sqlStatement = "select count(*) from Parking_Spot where status = 'empty' and Parking_Lot_id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, Integer.class, parkingLotId);
    }

    public int countAllSpots(int parkingLotId){
        String sqlStatement = "select count(*) from Parking_Spot where Parking_Lot_id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, Integer.class, parkingLotId);
    }


}
