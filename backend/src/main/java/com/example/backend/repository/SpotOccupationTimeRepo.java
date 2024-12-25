package com.example.backend.repository;


import com.example.backend.model.SpotOccupationTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class SpotOccupationTimeRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void save(SpotOccupationTime spotOccupationTime){
        String sqlStatement = "insert into Spot_Occupation_Time " +
                "(arrival_time, leave_time, id, Reserved_Spot_Driver_id, Reserved_Spot_Parking_Spot_Parking_Lot_id, Reserved_Spot_Parking_Spot_id)" +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlStatement,
                spotOccupationTime.getArrivalTime(),
                spotOccupationTime.getLeaveTime(),
                spotOccupationTime.getId(),
                spotOccupationTime.getReservedSpotDriverId(),
                spotOccupationTime.getReservedSpotParkingSpotParkingLotId(),
                spotOccupationTime.getReservedSpotParkingSpotId());

        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "occupied", spotOccupationTime.getReservedSpotParkingSpotId());
    }
    public void UpdateLeaveTime(int id, Timestamp leaveTime){
        String sqlStatement = "update Spot_Occupation_Time set leave_time = ? where id = ?";
        jdbcTemplate.update(sqlStatement, leaveTime, id);
        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "empty", id);
    }



}
