package com.example.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private AppUser user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(AppUser user) {
        this.user = user;
    }

    public CustomUserDetails(Object userEntity, String role) {
        if (userEntity instanceof Driver) {
            Driver driver = (Driver) userEntity;
            this.user = driver;
        } else if (userEntity instanceof LotManager) {
            LotManager manager = (LotManager) userEntity;
            this.user = manager;
        } else if (userEntity instanceof Administrator) {
            Administrator admin = (Administrator) userEntity;
            this.user = admin;
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }

        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public int getUserId() {
        return user.getId();
    }
}

