package com.NA.social.core.ultis;

import com.NA.social.core.entity.Message;
import com.NA.social.core.request.chat.ChatMessageEvent;
import com.NA.social.core.service.mesage.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchMessageSaver {
    private final MessageService messageService;
    private final List<ChatMessageEvent> messageQueue = new ArrayList<>();
    private final int QUEUE_SIZE_LIMIT = 100;

    @Scheduled(fixedDelay = 1000)
    public synchronized void saveMessages() {
        if (!messageQueue.isEmpty()) {
            messageService.saveAllMessages(messageQueue);
            messageQueue.clear();
        }
    }

    public synchronized Message addMessage(ChatMessageEvent event) {
        Message message = null;
        try {
            message = messageService.createMessageEntity(event);
            messageQueue.add(event);

            if (messageQueue.size() >= QUEUE_SIZE_LIMIT) {
                saveMessages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
