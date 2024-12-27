package com.example.backend.repository;

import com.example.backend.model.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ParkingLot> getParkingLotRevenues() {
        String sql = "SELECT id, longitude, latitude, revenue FROM Parking_Lot";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setId(rs.getInt("id"));
            parkingLot.setLongitude(rs.getString("longitude"));
            parkingLot.setLatitude(rs.getString("latitude"));
            parkingLot.setRevenue(rs.getDouble("revenue"));
            return parkingLot;
        });
    }

    public List<Map<String, Object>> getTopUsers() {
        String sql = "SELECT Driver_id, COUNT(*) AS reservations FROM Reserved_Spot " +
                "GROUP BY Driver_id " +
                "ORDER BY reservations DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    public double getOccupancyRate(int parkingLotId) {
        String sql = "SELECT COUNT(*) FROM Parking_Spot WHERE Parking_Lot_id = ? " +
                "AND status = 'Occupied'";
        Integer occupiedSpots = jdbcTemplate.queryForObject(sql, new Object[]{parkingLotId}, Integer.class);
        if (occupiedSpots == null) {
            occupiedSpots = 0;
        }
        String sql2 = "SELECT capacity FROM Parking_Lot WHERE id = ?";
        Integer totalSpots = jdbcTemplate.queryForObject(sql2, new Object[]{parkingLotId}, Integer.class);
        if (totalSpots == null) {
            totalSpots = 0;
        }
        return totalSpots > 0 ? (double) occupiedSpots / totalSpots * 100 : 0.0;
    }

    public double getTotalRevenue(int parkingLotId) {
        String sql = "SELECT SUM(price) FROM Reserved_Spot " +
                "WHERE Parking_Spot_Parking_Lot_id = ?";

        Double totalRevenue = jdbcTemplate.queryForObject(sql, new Object[]{parkingLotId}, Double.class);
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }
        return totalRevenue;
    }

    public double getTotalPenalties(int parkingLotId) {
        String sql = "SELECT SUM(penalty) FROM Reserved_Spot WHERE Parking_Spot_Parking_Lot_id = ? AND penalty IS NOT NULL";
        Double totalPenalties = jdbcTemplate.queryForObject(sql, new Object[]{parkingLotId}, Double.class);
        if (totalPenalties == null) {
            totalPenalties = 0.0;
        }
        return totalPenalties;
    }
}
