package com.NA.social.core.rabbit;

import com.NA.social.core.channel.MessageSocketChannel;
import com.NA.social.core.config.RabbitMQConfig;
import com.NA.social.core.entity.ChatMember;
import com.NA.social.core.entity.Message;
import com.NA.social.core.repository.ChatMemberRepository;
import com.NA.social.core.request.chat.ChatMessageEvent;
import com.NA.social.core.service.mesage.MessageService;
import com.NA.social.core.ultis.BatchMessageSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RabbitConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemberRepository chatMemberRepository;
    private final BatchMessageSaver batchMessageSaver;

    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE_NAME)
    public void receiveMessage(ChatMessageEvent event) throws Exception {
        Message message = batchMessageSaver.addMessage(event);
        // send to all user in chat ( non-active and active )
        List<ChatMember> chatMembers = chatMemberRepository.findAllByChatId(event.getChatId());
        for (ChatMember chatMember : chatMembers) {
            messagingTemplate.convertAndSend("/topic/user/" + chatMember.getUser().getUid() + "/chat-notify", message);
        }
        // send to active chat socket
        messagingTemplate.convertAndSend(MessageSocketChannel.CHAT_PATH + event.getChatId(), message);
    }
}
