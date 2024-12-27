package com.example.backend.service;

import com.example.backend.DTOs.ParkingLotManagerReportDTO;
import com.example.backend.DTOs.ReportUserDTO;
import com.example.backend.model.Driver;
import com.example.backend.model.ParkingLot;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Fetch top parking lot revenues
//    public List<ParkingLot> getTopParkingLotRevenues() {
//        String sql = "SELECT id, longitude, latitude, revenue FROM Parking_Lot " +
//                "ORDER BY revenue DESC LIMIT 10";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            ParkingLot parkingLot = new ParkingLot();
//            parkingLot.setId(rs.getInt("id"));
//            parkingLot.setLongitude(rs.getString("longitude"));
//            parkingLot.setLatitude(rs.getString("latitude"));
//            parkingLot.setRevenue(rs.getDouble("revenue"));
//            return parkingLot;
//        });
//    }

    public List<ParkingLot> getTopParkingLotRevenues() {
        String sql = "CALL GetTopParkingLotRevenues()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setId(rs.getInt("id"));
            parkingLot.setLongitude(rs.getString("longitude"));
            parkingLot.setLatitude(rs.getString("latitude"));
            parkingLot.setRevenue(rs.getDouble("revenue"));
            return parkingLot;
        });
    }

//    public List<ReportUserDTO> getTopUsers() {
//        String sql = "SELECT Driver_id AS driverId, COUNT(*) AS reservations " +
//                "FROM Reserved_Spot " +
//                "GROUP BY Driver_id " +
//                "ORDER BY reservations DESC " +
//                "LIMIT 10";
//
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            ReportUserDTO user = new ReportUserDTO();
//            user.setDriverId(rs.getInt("driverId"));
//            user.setReservations(rs.getInt("reservations"));
//            return user;
//        });
//    }

    public List<ReportUserDTO> getTopUsers() {
        String sql = "CALL GetTopUsers()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReportUserDTO user = new ReportUserDTO();
            user.setDriverId(rs.getInt("driverId"));
            user.setReservations(rs.getInt("reservations"));
            return user;
        });
    }



    // Generate PDF report for top parking lot revenues
    public byte[] generateReportParkingLotRevenues() throws Exception {
        List<ParkingLot> parkingLots = getTopParkingLotRevenues();
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/revenues_report.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(parkingLots);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "Top Parking Lot Revenues");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Generate PDF report for top users
    public byte[] generateReportTopUser() throws Exception {
        List<ReportUserDTO> topUsers = getTopUsers();
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/top_users_report.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topUsers);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "Top Users Reservations");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


//    public List<ParkingLotManagerReportDTO> getParkingLotManagerReport(int managerId) {
//        String sql = "SELECT " +
//                "pl.id AS parkingLotId, " +
//                "(COUNT(CASE WHEN ps.status = 'Occupied' THEN 1 END) / COUNT(*)) * 100 AS occupancy_rate, " +
//                "SUM(rs.price + COALESCE(rs.penalty, 0)) AS total_revenue, " +
//                "SUM(COALESCE(rs.penalty, 0)) AS total_violations " +
//                "FROM parkdb.Parking_Spot ps " +
//                "JOIN parkdb.Parking_Lot pl ON ps.Parking_Lot_id = pl.id " +
//                "LEFT JOIN parkdb.Reserved_Spot rs ON ps.Parking_Lot_id = rs.Parking_Spot_Parking_Lot_id " +
//                "AND ps.id = rs.Parking_Spot_id " +
//                "WHERE pl.Parking_Lot_Manager_id = ? " +
//                "GROUP BY pl.id";
//
//        return jdbcTemplate.query(sql, new Object[]{managerId}, new RowMapper<ParkingLotManagerReportDTO>() {
//            @Override
//            public ParkingLotManagerReportDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//                ParkingLotManagerReportDTO report = new ParkingLotManagerReportDTO();
//                report.setParkingLotId(rs.getInt("parkingLotId"));
//                report.setOccupancyRate(rs.getDouble("occupancy_rate"));
//                report.setTotalRevenue(rs.getDouble("total_revenue"));
//                report.setTotalViolations(rs.getDouble("total_violations"));
//                return report;
//            }
//        });
//    }

    public List<ParkingLotManagerReportDTO> getParkingLotManagerReport(int managerId) {
        String sql = "SELECT pl.id AS parkingLotId, " +
                "COUNT(CASE WHEN ps.status = 'occupied' THEN 1 END) / COUNT(ps.id) * 100 " +
                "AS occupied_rate, pl.revenue AS total_revenue, SUM(rs.penalty) " +
                "AS total_violations FROM Parking_Lot pl LEFT JOIN Parking_Spot ps " +
                "ON pl.id = ps.Parking_Lot_id LEFT JOIN Reserved_Spot rs " +
                "ON ps.id = rs.Parking_Spot_id WHERE pl.Parking_Lot_Manager_id = ? " +
                "GROUP BY pl.id, pl.revenue";

        // Return an empty list if no results are found.
        return jdbcTemplate.query(sql, new Object[]{managerId}, new RowMapper<ParkingLotManagerReportDTO>() {
            @Override
            public ParkingLotManagerReportDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ParkingLotManagerReportDTO report = new ParkingLotManagerReportDTO();
                report.setParkingLotId(rs.getInt("parkingLotId"));
                report.setOccupancyRate(rs.getDouble("occupied_rate"));
                report.setTotalRevenue(rs.getDouble("total_revenue"));
                report.setTotalViolations(rs.getDouble("total_violations"));
                return report;
            }
        });
    }



    public byte[] generateParkingLotManagerReport(int managerId) throws Exception {
        List<ParkingLotManagerReportDTO> reportData = getParkingLotManagerReport(managerId);
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/manager_report.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "Parking Lot Manager Real-Time Report");
        parameters.put("managerId", managerId);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}