package com.example.backend.repository;

import com.example.backend.DTOs.LotCreationDTO;
import com.example.backend.DTOs.ParkingLotResponseDTO;
import com.example.backend.model.ParkingLot;
import com.example.backend.model.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingLotRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(LotCreationDTO parkingLot, int managerId){
        System.out.println(managerId);
        String sqlStatement = "insert into Parking_Lot " +
                "(longitude, latitude , capacity, price, type , revenue , Parking_Lot_Manager_id ) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlStatement,
                parkingLot.getLongitude(),
                parkingLot.getLatitude(),
                parkingLot.getNumberOfSlots(),
                parkingLot.getPricePerHour(),
                parkingLot.getParkingType(),
                0.0,
                managerId);

        int parking_lot_id = getLastParkingLotId();
        System.out.println("yarab");
        System.out.println(parking_lot_id);

        for(int i = 0; i < parkingLot.getNumberOfSlots(); i++){
            String sqlStatement2 = "insert into Parking_Spot " +
                    "(id, status, Parking_Lot_id) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlStatement2, i+1, "empty", parking_lot_id);
        }
    }

    private int getLastParkingLotId() {
        String sql = "SELECT id FROM parkdb.Parking_Lot ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void delete(int id){
        String sqlStatement = "delete from Parking_Lot where id = ?";
        jdbcTemplate.update(sqlStatement, id);
    }

    public Boolean isParkingLotFound(String latitude, String longitude) {
        String sqlStatement = "SELECT COUNT(*) FROM parkdb.Parking_Lot WHERE latitude = ? AND longitude = ?";
        Integer count = jdbcTemplate.queryForObject(sqlStatement, new Object[]{latitude, longitude}, Integer.class);
        return count > 0;
    }

    public ParkingLotResponseDTO getParkingLotInfo(String latitude, String longitude) {
        String parkingLotQuery = "SELECT longitude, latitude, price AS pricePerHour, type AS parkingType " +
                "FROM parkdb.Parking_Lot WHERE latitude = ? AND longitude = ?";

        ParkingLotResponseDTO parkingLot = jdbcTemplate.queryForObject(parkingLotQuery, new Object[]{latitude, longitude},
                (rs, rowNum) -> {
                    ParkingLotResponseDTO dto = new ParkingLotResponseDTO();
                    dto.setLongitude(rs.getString("longitude"));
                    dto.setLatitude(rs.getString("latitude"));
                    dto.setPricePerHour(rs.getDouble("pricePerHour"));
                    dto.setParkingType(rs.getString("parkingType"));
                    return dto;
                });

        String parkingSpotsQuery = "SELECT id, status FROM parkdb.Parking_Spot WHERE Parking_Lot_id = ( " +
                "SELECT id FROM parkdb.Parking_Lot WHERE latitude = ? AND longitude = ? )";

        List<ParkingSpot> parkingSpots = jdbcTemplate.query(parkingSpotsQuery, new Object[]{latitude, longitude},
                (rs, rowNum) -> {
                    ParkingSpot spot = new ParkingSpot();
                    spot.setId(rs.getInt("id"));
                    spot.setStatus(rs.getString("status"));
                    return spot;
                });

        parkingLot.setParkingSpots(parkingSpots);

        return parkingLot;
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
