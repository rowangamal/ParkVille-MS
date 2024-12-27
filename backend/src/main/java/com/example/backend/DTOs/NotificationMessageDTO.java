package com.example.backend.DTOs;

public class NotificationMessageDTO {
    private String message;

    public NotificationMessageDTO(String message) {
        this.message = message;
    }

    public NotificationMessageDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
