package com.example.backend.service;

import com.example.backend.DTOs.ReportUserDTO;
import com.example.backend.model.Driver;
import com.example.backend.model.ParkingLot;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Fetch top parking lot revenues
    public List<ParkingLot> getTopParkingLotRevenues() {
        String sql = "SELECT id, longitude, latitude, revenue FROM Parking_Lot " +
                "ORDER BY revenue DESC LIMIT 10";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setId(rs.getInt("id"));
            parkingLot.setLongitude(rs.getString("longitude"));
            parkingLot.setLatitude(rs.getString("latitude"));
            parkingLot.setRevenue(rs.getDouble("revenue"));
            return parkingLot;
        });
    }

    public List<ReportUserDTO> getTopUsers() {
        String sql = "SELECT Driver_id AS driverId, COUNT(*) AS reservations " +
                "FROM Reserved_Spot " +
                "GROUP BY Driver_id " +
                "ORDER BY reservations DESC " +
                "LIMIT 10";

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

}
