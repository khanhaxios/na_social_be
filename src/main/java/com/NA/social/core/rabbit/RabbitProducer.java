package com.NA.social.core.rabbit;

import com.NA.social.core.config.RabbitMQConfig;
import com.NA.social.core.request.chat.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(ChatMessageEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE_NAME, RabbitMQConfig.MESSAGE_ROUTING_KEY, event);
    }

}
