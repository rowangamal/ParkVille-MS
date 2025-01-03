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
                "(arrival_time, Reserved_Spot_Driver_id, Reserved_Spot_Parking_Spot_Parking_Lot_id, Reserved_Spot_Parking_Spot_id)" +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlStatement,
                spotOccupationTime.getArrivalTime(),
                spotOccupationTime.getReservedSpotDriverId(),
                spotOccupationTime.getReservedSpotParkingSpotParkingLotId(),
                spotOccupationTime.getReservedSpotParkingSpotId());

//        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "occupied", spotOccupationTime.getReservedSpotParkingSpotId());
    }
    public void UpdateLeaveTime(SpotOccupationTime spotOccupationTime, Timestamp leaveTime){
        String sqlStatement = "update Spot_Occupation_Time set leave_time = ? where Reserved_Spot_Driver_id = ? "+
                "and Reserved_Spot_Parking_Spot_Parking_Lot_id = ? and Reserved_Spot_Parking_Spot_id = ?";
        jdbcTemplate.update(sqlStatement, leaveTime, spotOccupationTime.getReservedSpotDriverId(),
                spotOccupationTime.getReservedSpotParkingSpotParkingLotId(), spotOccupationTime.getReservedSpotParkingSpotId());
//        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "empty", id);
    }

    public SpotOccupationTime getSpotOccupationTime(int driverId, int parkingSpotParkingLotId, int parkingSpotId){
        String sqlStatement = "select * from Spot_Occupation_Time where Reserved_Spot_Driver_id = ? and Reserved_Spot_Parking_Spot_Parking_Lot_id = ? and Reserved_Spot_Parking_Spot_id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, new Object[]{driverId, parkingSpotParkingLotId, parkingSpotId}, (resultSet, i) -> {
            return new SpotOccupationTime(
                    resultSet.getInt("Reserved_Spot_Driver_id"),
                    resultSet.getInt("Reserved_Spot_Parking_Spot_id"),
                    resultSet.getInt("Reserved_Spot_Parking_Spot_Parking_Lot_id"),
                    resultSet.getTimestamp("arrival_time")
            );
        });
    }



}
