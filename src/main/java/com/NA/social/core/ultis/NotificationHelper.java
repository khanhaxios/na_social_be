package com.NA.social.core.ultis;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHelper {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public  void sendNotification(String path, Object data) {
        simpMessagingTemplate.convertAndSend(path, data);
    }

    public void sendNotificationToUser(String username, String path, Object data) {
        simpMessagingTemplate.convertAndSendToUser(username, "/queue" + path, data);
    }
}
