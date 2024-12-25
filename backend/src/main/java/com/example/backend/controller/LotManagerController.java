package com.example.backend.controller;
import com.example.backend.model.LotManager;
import com.example.backend.service.LotManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managers/")
public class LotManagerController {
    @Autowired
    private LotManagerService LotManagerService;

    @PostMapping("/signup")
    public String signup(@RequestBody LotManager lotManager) {
        System.out.println("i am lot manager");
        return LotManagerService.signup(lotManager);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return LotManagerService.login(username, password);
    }
}
