package com.example.backend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessLoginDTO {
    private int id;
    private String username;
    private String role;
    private String jwtToken;

    public SuccessLoginDTO( int id, String username, String role, String jwtToken) {
        this.username = username;
        this.role = role;
        this.jwtToken = jwtToken;
        this.id = id;
    }

}
