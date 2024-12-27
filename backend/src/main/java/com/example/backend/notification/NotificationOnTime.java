package com.example.backend.notification;


import com.example.backend.DTOs.DriverNotificationDTO;
import com.example.backend.DTOs.NotificationMessageDTO;
import com.example.backend.repository.ReservedSpotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationOnTime {
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    public NotificationOnTime(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private ReservedSpotRepo reservedSpotRepo;
    @Scheduled(fixedRate = 1000  * 60 )
    public void arriveAt10Min() {
        List<DriverNotificationDTO> driverNotificationDTOList = reservedSpotRepo.getAllArrivingWithin10Minutes();
        for (DriverNotificationDTO driverNotificationDTO : driverNotificationDTOList) {
            NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO("You should arrive at your spot in "+ driverNotificationDTO.getTimeDiff() + " minutes");
            messagingTemplate.convertAndSend("/topic/notification/" + driverNotificationDTO.getDriverId()
                    , notificationMessageDTO);
        }
    }
    @Scheduled(fixedRate = 1000  * 60 )
    public void LeaveWithin10Min() {
        List<DriverNotificationDTO> driverNotificationDTOList = reservedSpotRepo.getAllLeavingWithin10Minutes();
        for (DriverNotificationDTO driverNotificationDTO : driverNotificationDTOList) {
            NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO("You have to leave within "+ driverNotificationDTO.getTimeDiff() + " minutes");
            messagingTemplate.convertAndSend("/topic/notification/" + driverNotificationDTO.getDriverId()
                    , notificationMessageDTO);
        }
    }
    @Scheduled(fixedRate = 1000  * 60 )
    public void penaltyOverTime() {
        List<DriverNotificationDTO> driverNotificationDTOList = reservedSpotRepo.getPenaltyOverTime();
        for (DriverNotificationDTO driverNotificationDTO : driverNotificationDTOList) {
            NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO("you have a penalty for late leaving for "+ driverNotificationDTO.getTimeDiff() + " minutes with " + driverNotificationDTO.getPenalty() + " dollars");
            messagingTemplate.convertAndSend("/topic/notification/" + driverNotificationDTO.getDriverId()
                    , notificationMessageDTO);
        }
    }
}
