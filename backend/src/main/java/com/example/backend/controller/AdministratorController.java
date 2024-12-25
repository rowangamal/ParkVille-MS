package com.example.backend.controller;
import com.example.backend.model.Administrator;
import com.example.backend.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestParam String username, @RequestParam String password) {
        return administratorService.login(username, password);
    }
}
