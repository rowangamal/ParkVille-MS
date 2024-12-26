package com.example.backend.service;

import com.example.backend.model.Administrator;
import com.example.backend.model.CustomUserDetails;
import com.example.backend.model.Driver;
import com.example.backend.model.LotManager;
import com.example.backend.repository.AdministratorRepo;
import com.example.backend.repository.DriverRepo;
import com.example.backend.repository.LotManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    private LotManagerRepo lotManagerRepo;
    @Autowired
    private AdministratorRepo administratorRepo;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByIdAndRole(int id, String role) {
        return switch (role) {
            case "Driver" -> new CustomUserDetails(driverRepo.getDriverById(id));
            case "Administrator" -> new CustomUserDetails(administratorRepo.findAdminById(id));
            case "Manager" -> new CustomUserDetails(lotManagerRepo.findManagerById(id));
            default -> null;
        };
    }
    public CustomUserDetails loadUserDetailsByIdAndRole(int userId, String role) {
        switch (role) {
            case "ROLE_DRIVER": {
                Driver driver = driverRepo.getDriverById(userId);
                if (driver == null) {
                    throw new UsernameNotFoundException("Driver not found with ID: " + userId);
                }
                return new CustomUserDetails(driver, "ROLE_DRIVER");
            }
            case "ROLE_MANAGER": {
                LotManager manager = lotManagerRepo.findManagerById(userId);
                if (manager == null) {
                    throw new UsernameNotFoundException("Manager not found with ID: " + userId);
                }
                return new CustomUserDetails(manager, "ROLE_MANAGER");
            }
            case "ROLE_ADMIN": {
                Administrator admin = administratorRepo.findAdminById(userId);
                if (admin == null) {
                    throw new UsernameNotFoundException("Administrator not found with ID: " + userId);
                }
                return new CustomUserDetails(admin, "ROLE_ADMIN");
            }
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

}
