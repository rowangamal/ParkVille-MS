package com.example.backend.service;

import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.CustomUserDetails;
import com.example.backend.model.LotManager;
import com.example.backend.repository.LotManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotManagerService {
    @Autowired
    private LotManagerRepo lotManagerRepo;
    @Autowired
    private JWTService jwtService;

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

    public SuccessLoginDTO login(String username, String password) {
        List<LotManager> existingLotManagers = lotManagerRepo.getAll();
        Optional<LotManager> matchingManager = existingLotManagers.stream()
                .filter(l -> l.getUsername().equals(username))
                .findFirst();

        if (matchingManager.isEmpty()) {
            throw  new RuntimeException("Invalid email or password.");
        }

        LotManager lotManager = matchingManager.get();
        if (!lotManager.getPassword().equals(password)) {
            throw  new RuntimeException("Invalid email or password.");
        }
        String jwt = jwtService.generateToken(lotManager.getId(), lotManager.getRole());
        return new SuccessLoginDTO(lotManager.getId(), lotManager.getUsername(), lotManager.getRole(), jwt);
    }

    public int getManagerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated or invalid principal");
        }
        return ((CustomUserDetails)authentication.getPrincipal()).getUserId();
    }
}
