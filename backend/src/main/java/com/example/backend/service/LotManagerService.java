package com.example.backend.service;

import com.example.backend.model.LotManager;
import com.example.backend.repository.LotManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotManagerService {
    @Autowired
    private LotManagerRepo lotManagerRepo;

    public String signup(LotManager lotManager) {
        List<LotManager> existingLotManagers = lotManagerRepo.getAll();
        Optional<LotManager> existingManager = existingLotManagers.stream()
                .filter(l -> l.getUsername().equals(lotManager.getUsername()))
                .findFirst();

        if (existingManager.isPresent()) {
            return "Username already exists.";
        }

        lotManagerRepo.save(lotManager);
        return "Signup successful.";
    }

    public String login(String username, String password) {
        List<LotManager> existingLotManagers = lotManagerRepo.getAll();
        Optional<LotManager> matchingManager = existingLotManagers.stream()
                .filter(l -> l.getUsername().equals(username))
                .findFirst();

        if (matchingManager.isEmpty()) {
            return "Invalid username.";
        }

        LotManager lotManager = matchingManager.get();
        if (!lotManager.getPassword().equals(password)) {
            return "Invalid password.";
        }
        return "Login successful.";
    }
}
