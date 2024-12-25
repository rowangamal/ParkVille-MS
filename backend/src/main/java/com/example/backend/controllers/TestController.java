package com.example.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;

@RestController("/test")
@CrossOrigin("*")
public class TestController {

    @PostMapping("")
    public ResponseEntity<String> test() {

        try {
            return ResponseEntity.ok("Test successful");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
