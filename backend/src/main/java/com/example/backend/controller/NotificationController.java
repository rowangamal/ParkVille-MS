package com.example.backend.controller;

import com.example.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Thread.sleep;

@RestController // Use RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    //    @SendTo("/topic/notification")
//    public String notify(String message) {
//        System.out.println("Received message: " + message);
//        return message;
//    }
    @GetMapping("/sendNotification")
    public ResponseEntity<String> sendNotification() {
        System.out.println("Sending notification...");
        notificationService.sendNotification("{\"message\": \"Hello from the server!\"}");
        return new ResponseEntity<>("Notification sent!", HttpStatus.OK);
    }
}