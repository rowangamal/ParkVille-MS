package com.example.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired

    public NotificationService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String message){
        messagingTemplate.convertAndSend("/topic/notification", message);
    }
}
