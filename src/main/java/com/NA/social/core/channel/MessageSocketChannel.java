package com.NA.social.core.channel;


import com.NA.social.core.entity.Message;
import com.NA.social.core.enums.MessageStatus;
import com.NA.social.core.repository.ChatRepository;
import com.NA.social.core.repository.MediaRepository;
import com.NA.social.core.repository.MessageRepository;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.chat.SendMessageRequest;
import com.NA.social.core.ultis.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;

@Controller
@RequiredArgsConstructor
public class MessageSocketChannel {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatRepository chatRepository;
    private final MediaRepository mediaRepository;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId, SendMessageRequest request) {
        Message message = Message.builder()
                .content(request.getContent())
                .media(new HashSet<>(mediaRepository.findAllById(request.getMediaId())))
                .messageType(request.getMessageType())
                .chat(chatRepository.findById(request.getChatId()).orElse(null))
                .replyTo(userRepository.findById(request.getUserReplyId()).orElse(null))
                .sender(userRepository.findById(request.getSenderId()).orElse(null))
                .build();
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, messageRepository.save(message));

    }
}
