package com.example.backend.model;

public interface AppUser {
    int getId();
    String getUsername();
    String getEmail();
    String getPassword();
    String getRole(); // e.g., "Driver", "SystemAdministrator"
}
