package com.example.backend.repository;


import com.example.backend.model.ReservedSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservedSpotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ReservedSpot reservedSpot){
        String sqlStatement = "insert into Reserved_Spot " +
                "(Parking_Spot_id, Parking_Spot_Parking_Lot_id ,Driver_id ,start_time , end_time)" +
                "values (?, ? , ? , ? , ?)";
        jdbcTemplate.update(sqlStatement,
                reservedSpot.getParkingSpotId(),
                reservedSpot.getParkingSpotParkingLotId(),
                reservedSpot.getDriverId(),
                reservedSpot.getStartTime(),
                reservedSpot.getEndTime());
        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "reserved", reservedSpot.getParkingSpotId());
    }
    public void delete(int id){
        String sqlStatement = "delete from Reserved_Spot where id = ?";
        jdbcTemplate.update(sqlStatement, id);
        jdbcTemplate.update("update Parking_Spot set status = ? where id = ?", "empty", id);
    }


}