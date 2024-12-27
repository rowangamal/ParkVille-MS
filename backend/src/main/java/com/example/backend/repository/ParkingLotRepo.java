package com.example.backend.repository;


import com.example.backend.model.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ParkingLotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(ParkingLot parkingLot ){
        String sqlStatement = "insert into Parking_Lot " +
                "(longitude, latitude , capacity, price, type , revenue , Parking_Lot_Manager_id ) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlStatement,
                parkingLot.getLongitude(),
                parkingLot.getLatitude(),
                parkingLot.getCapacity(),
                parkingLot.getPrice(),
                parkingLot.getType(),
                parkingLot.getRevenue(),
                parkingLot.getParkingLotManagerid());
        for(int i = 0; i < parkingLot.getCapacity(); i++){
            String sqlStatement2 = "insert into Parking_Spot " +
                    "(status, Parking_Lot_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlStatement2, "empty", parkingLot.getId());
        }
    }
    public void delete(int id){
        String sqlStatement = "delete from Parking_Lot where id = ?";
        jdbcTemplate.update(sqlStatement, id);

    }

    public ParkingLot getParkingLotById(int id){
        String sqlStatement = "select * from Parking_Lot where id = ?";
        return jdbcTemplate.queryForObject(sqlStatement, new Object[]{id}, (resultSet, i) -> {
            return new ParkingLot(
                    resultSet.getInt("id"),
                    resultSet.getString("longitude"),
                    resultSet.getString("latitude"),
                    resultSet.getInt("capacity"),
                    resultSet.getDouble("price"),
                    resultSet.getString("type"),
                    resultSet.getDouble("revenue"),
                    resultSet.getInt("Parking_Lot_Manager_id")
            );
        });
    }

}
