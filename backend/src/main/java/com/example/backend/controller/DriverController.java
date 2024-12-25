package com.example.backend.controller;

import com.example.backend.model.Driver;
import com.example.backend.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestParam String username, @RequestParam String password) {
        return driverService.login(username, password);
    }
}
