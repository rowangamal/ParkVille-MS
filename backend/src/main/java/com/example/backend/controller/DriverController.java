package com.example.backend.controller;

import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.Driver;
import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/signup")
    public String signup(@RequestBody Driver driver) {
        System.out.println("i am driver");
        return driverService.signup(driver);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            SuccessLoginDTO successLoginDTO = driverService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(successLoginDTO);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
