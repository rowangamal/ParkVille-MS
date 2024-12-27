package com.example.backend.controller;

import com.example.backend.service.LotManagerService;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    LotManagerService lotManagerService;

    @GetMapping("/getReport")
    public ResponseEntity<byte[]> generateReportLotRevenues() {
        try {
            byte[] pdfReport = reportService.generateReportParkingLotRevenues();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report.pdf");
            return ResponseEntity.ok().headers(headers).contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(pdfReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/topUsers")
    public ResponseEntity<byte[]> generateReportTopUsers() {
        try {
            byte[] pdfReport = reportService.generateReportTopUser();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=top_users_report.pdf");
            return ResponseEntity.ok().headers(headers).contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(pdfReport);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/parking-lot-manager")
    public ResponseEntity<byte[]> generateParkingLotManagerReport() throws Exception {
        byte[] reportContent = reportService.generateParkingLotManagerReport(lotManagerService.getManagerId());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/pdf");
        headers.set("Content-Disposition", "attachment; filename=\"parking_lot_manager_report_%d.pdf\"".formatted(lotManagerService.getManagerId()));
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }
}