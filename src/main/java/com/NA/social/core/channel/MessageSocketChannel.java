package com.NA.social.core.channel;


import com.NA.social.core.config.RabbitMQConfig;
import com.NA.social.core.rabbit.RabbitProducer;
import com.NA.social.core.request.chat.ChatMessageEvent;
import com.NA.social.core.request.chat.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MessageSocketChannel {
    public static final String CHAT_PATH = "/topic/chat/";
    private final RabbitProducer producer;

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId, SendMessageRequest request) {
        producer.sendMessage(new ChatMessageEvent(chatId, request));
    }
}
