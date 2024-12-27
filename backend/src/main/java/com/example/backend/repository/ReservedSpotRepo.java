package com.example.backend.repository;


import com.example.backend.DTOs.DriverNotificationDTO;
import com.example.backend.model.ReservedSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservedSpotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ReservedSpot reservedSpot){
        String sqlStatement = "insert into Reserved_Spot " +
                "(Parking_Spot_id, Parking_Spot_Parking_Lot_id ,Driver_id ,start_time , end_time, price)" +
                "values (?, ? , ? , ? , ?, ?)";
        jdbcTemplate.update(sqlStatement,
                reservedSpot.getParkingSpotId(),
                reservedSpot.getParkingSpotParkingLotId(),
                reservedSpot.getDriverId(),
                reservedSpot.getStartTime(),
                reservedSpot.getEndTime(),
                reservedSpot.getPrice());

//        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "reserved", reservedSpot.getParkingSpotId());
    }
//    public void delete(int id){
//        String sqlStatement = "delete from Reserved_Spot where id = ?";
//        jdbcTemplate.update(sqlStatement, id);
//        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "empty", id);
//    }

    public void delete(int driverId, int parkingSpotId, int parkingLotId){
        String sqlStatement = "delete from Reserved_Spot where Driver_id = ? and Parking_Spot_id = ? and Parking_Spot_Parking_Lot_id = ?";
        jdbcTemplate.update(sqlStatement, driverId, parkingSpotId, parkingLotId);
        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "empty", parkingSpotId);
    }

    public ReservedSpot getReservedSpot(int driverId, int parkingSpotId, int parkingLotId){
        String sqlStatement = "select * from Reserved_Spot where Driver_id = ? and Parking_Spot_id = ? and Parking_Spot_Parking_Lot_id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, new Object[]{driverId, parkingSpotId, parkingLotId}, (resultSet, i) -> {
            return new ReservedSpot(
                    resultSet.getTimestamp("end_time"),
                    resultSet.getTimestamp("start_time"),
                    resultSet.getTimestamp("arrival_time"),
                    resultSet.getTimestamp("leave_time"),
                    resultSet.getInt("Driver_id"),
                    resultSet.getInt("Parking_Spot_Parking_Lot_id"),
                    resultSet.getInt("Parking_Spot_id"),
                    resultSet.getDouble("price"),
                    resultSet.getDouble("penalty")

            );
        });
    }

    public void updateArrivalTime(ReservedSpot reservedSpot){
        String sqlStatement = "update Reserved_Spot set arrival_time = ? where Driver_id = ? and Parking_Spot_id = ? and Parking_Spot_Parking_Lot_id = ?";
        jdbcTemplate.update(sqlStatement, reservedSpot.getArrivalTime(), reservedSpot.getDriverId(), reservedSpot.getParkingSpotId(), reservedSpot.getParkingSpotParkingLotId());
    }

    public void updateLeaveTime(ReservedSpot reservedSpot){
        String sqlStatement = "update Reserved_Spot set leave_time = ? where Driver_id = ? and Parking_Spot_id = ? and Parking_Spot_Parking_Lot_id = ?";
        jdbcTemplate.update(sqlStatement, reservedSpot.getLeaveTime(), reservedSpot.getDriverId(), reservedSpot.getParkingSpotId(), reservedSpot.getParkingSpotParkingLotId());
    }


    public List<DriverNotificationDTO> getAllArrivingWithin10Minutes(){
        String sql = "{call getAllArrivingWithin10Min()}";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new DriverNotificationDTO(
                    resultSet.getInt("Driver_id"),
                    resultSet.getInt("time_diff")
            );
        });
    }
    public List<DriverNotificationDTO> getAllLeavingWithin10Minutes(){
        String sql = "{call getAllLeavingWithin10Min()}";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new DriverNotificationDTO(
                    resultSet.getInt("Driver_id"),
                    resultSet.getInt("time_diff")
            );
        });
    }
    public List<DriverNotificationDTO> getPenaltyOverTime(){
        String sql = "{call getPenaltyOverTime()}";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new DriverNotificationDTO(
                    resultSet.getInt("Driver_id"),
                    resultSet.getInt("time_diff"),
                    resultSet.getInt("penalty")
            );
        });
    }


}

