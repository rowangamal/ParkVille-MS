package com.example.backend.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component

public class Driver implements AppUser {
    private int id;
    private String username;
    private String email;
    private String licensePlateNumber;
    private String password;
    private String paymentMethod;

    public Driver(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Driver{id=%d, username='%s', email='%s', licensePlateNumber='%s', password='%s', paymentMethod='%s'}".formatted(id, username, email, licensePlateNumber, password, paymentMethod);
    }

    @Override
    public String getRole() {
        return "ROLE_DRIVER";
    }
}
