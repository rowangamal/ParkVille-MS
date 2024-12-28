package com.example.backend.service;

import com.example.backend.DTOs.SuccessLoginDTO;
import com.example.backend.model.Administrator;
import com.example.backend.model.CustomUserDetails;
import com.example.backend.repository.AdministratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        administrator.setPassword(encoder.encode(administrator.getPassword()));
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

        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        Administrator administrator = matchingAdmin.get();
        if (!encoder.matches(password, administrator.getPassword())) {
            throw  new RuntimeException("Invalid email or password.");
        }

        String jwt = jwtService.generateToken(administrator.getId(), administrator.getRole());
        return new SuccessLoginDTO(administrator.getId(), administrator.getUsername(), administrator.getRole(), jwt);
    }

    public int getAdministratorId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated or invalid principal");
        }
        return ((CustomUserDetails)authentication.getPrincipal()).getUserId();
    }
}
