package com.ecom2.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements  NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 60000)
    public void sendNotificationEveryMinute() {
        String message = "Notification at: " + LocalDateTime.now();

        log.info("hihihi");

        messagingTemplate.convertAndSendToUser(
                "user011",
                "/queue/private",
                message);


    }
}
