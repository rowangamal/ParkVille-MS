package com.example.backend.service;

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

    public String login(String username, String password) {
        List<Administrator> existingAdmins = administratorRepo.getAll();
        Optional<Administrator> matchingAdmin = existingAdmins.stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst();

        if (matchingAdmin.isEmpty()) {
            return "Invalid username.";
        }

        Administrator administrator = matchingAdmin.get();
        if (!administrator.getPassword().equals(password)) {
            return "Invalid password.";
        }

        return "Login successful.";
    }
}
