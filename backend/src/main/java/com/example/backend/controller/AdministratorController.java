package com.example.backend.controller;
import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.model.Administrator;
import com.example.backend.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @PostMapping("/signup")
    public String signup(@RequestBody Administrator administrator) {
        System.out.println("i am admin");
        return administratorService.signup(administrator);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequest) {
        String message = administratorService.login(loginRequest.getUsername(), loginRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        HttpStatus status = message.equals("Login successful.") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
}
