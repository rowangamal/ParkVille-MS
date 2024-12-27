package com.example.backend.service;

import com.example.backend.model.ParkingLot;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] generateParkingLotPerformanceReport() throws Exception {
        String jrxmlPath = "C:\\Users\\Yousef\\Desktop\\SmartPark-MS\\backend\\src\\ParkingLotPerformance.jrxml";
        System.out.printf("JRXML Path: %s%n", jrxmlPath);

        File reportFile = new File(jrxmlPath);
        if (!reportFile.exists()) {
            System.err.printf("Report template not found: %s%n", jrxmlPath);
            throw new Exception("Report template not found at: %s".formatted(jrxmlPath));
        }
        System.out.println("Report template found.");
        InputStream inputStream = new FileInputStream(reportFile);
        System.out.printf("Input Stream length: %d bytes.%n", inputStream.available());
        JasperReport jasperReport = compileReport(inputStream);
        System.out.println("Report compiled successfully.");
        List<ParkingLot> parkingLotData = getParkingLotPerformanceData();
        System.out.printf("Parking Lot Data fetched: %d records.%n", parkingLotData.size());
        for (ParkingLot parkingLot : parkingLotData) {
            System.out.printf("Parking Lot ID: %d, Revenue: %s%n", parkingLot.getId(), parkingLot.getRevenue());
        }

        JRDataSource dataSource = new JRBeanCollectionDataSource(parkingLotData);
        JasperPrint jasperPrint = fillReport(jasperReport, dataSource);
        System.out.println("Report filled successfully.");
        byte[] pdfReport = exportReportToPdf(jasperPrint);
        System.out.printf("Generated PDF Report of size: %d bytes.%n", pdfReport.length);

        return pdfReport;
    }



    // Compile the JRXML template into a JasperReport object
    private JasperReport compileReport(InputStream inputStream) throws Exception {
        try {
            return JasperCompileManager.compileReport(inputStream);
        } catch (JRException e) {
            System.err.printf("Error compiling report: %s%n", e.getMessage());
            throw new Exception("Error compiling report: %s".formatted(e.getMessage()), e);
        }
    }

    // Fill the report with data
    private JasperPrint fillReport(JasperReport jasperReport, JRDataSource dataSource) throws Exception {
        try {
            Map<String, Object> parameters = Map.of();
            return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            System.err.printf("Error filling report: %s%n", e.getMessage());
            throw new Exception("Error filling report: %s".formatted(e.getMessage()), e);
        }
    }

    // Export the filled report to PDF
    private byte[] exportReportToPdf(JasperPrint jasperPrint) throws Exception {
        try {
            System.out.println("Exporting report to PDF...");
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            System.out.printf("PDF Exported. PDF size: %d bytes.%n", pdfBytes.length);
            return pdfBytes;
        } catch (JRException e) {
            System.err.printf("Error exporting report to PDF: %s%n", e.getMessage());
            throw new Exception("Error exporting report to PDF: %s".formatted(e.getMessage()), e);
        }
    }


    // Fetch parking lot performance data from the database
    private List<ParkingLot> getParkingLotPerformanceData() {
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
}
