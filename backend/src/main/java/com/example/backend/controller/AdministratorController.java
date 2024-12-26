package com.example.backend.controller;
import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.DTOs.SuccessLoginDTO;
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
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequest) {
        try{
            SuccessLoginDTO successLoginDTO = administratorService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(successLoginDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
