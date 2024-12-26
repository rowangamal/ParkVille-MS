package com.example.backend.controller;
import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.LotManager;
import com.example.backend.service.LotManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/managers/")
public class LotManagerController {
    @Autowired
    private LotManagerService lotManagerService;

    @PostMapping("/signup")
    public String signup(@RequestBody LotManager lotManager) {
        System.out.println("i am lot manager");
        return lotManagerService.signup(lotManager);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequest) {
        try{
            SuccessLoginDTO successLoginDTO = lotManagerService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(successLoginDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
