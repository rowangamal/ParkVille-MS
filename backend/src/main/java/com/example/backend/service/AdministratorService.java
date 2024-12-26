package com.example.backend.service;

import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.Administrator;
import com.example.backend.repository.AdministratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepo administratorRepo;

    @Autowired
    private JWTService jwtService;

    public String signup(Administrator administrator) {
        List<Administrator> existingAdmins = administratorRepo.getAll();
        Optional<Administrator> existingAdmin = existingAdmins.stream()
                .filter(a -> a.getUsername().equals(administrator.getUsername()))
                .findFirst();

        if (existingAdmin.isPresent()) {
            return "Username already exists.";
        }

        administratorRepo.save(administrator);
        return "Signup successful.";
    }

    public SuccessLoginDTO login(String username, String password) {
        List<Administrator> existingAdmins = administratorRepo.getAll();
        Optional<Administrator> matchingAdmin = existingAdmins.stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst();

        if (matchingAdmin.isEmpty()) {
            throw  new RuntimeException("Invalid email or password.");
        }

        Administrator administrator = matchingAdmin.get();
        if (!administrator.getPassword().equals(password)) {
            throw  new RuntimeException("Invalid email or password.");
        }

        String jwt = jwtService.generateToken(administrator.getId(), administrator.getRole());
        return new SuccessLoginDTO(administrator.getId(), administrator.getUsername(), administrator.getRole(), jwt);
    }
}
