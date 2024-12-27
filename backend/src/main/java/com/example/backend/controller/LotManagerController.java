package com.example.backend.controller;
import com.example.backend.DTOs.LoginRequestDTO;
import com.example.backend.DTOs.LotCreationDTO;
import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.LotManager;
import com.example.backend.service.JWTService;
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

    @Autowired
    private JWTService jwtService;

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

    @PostMapping("/create-parking-lot")
    public ResponseEntity<String> createParkingLot(
            @RequestBody LotCreationDTO lotCreationRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        int managerId = jwtService.extractUserId(token);
        lotManagerService.createParkingLot(lotCreationRequest, managerId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Parking data saved successfully!");
    }
}
