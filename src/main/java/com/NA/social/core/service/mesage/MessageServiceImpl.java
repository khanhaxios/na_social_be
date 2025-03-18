package com.NA.social.core.service.mesage;

import com.NA.social.core.channel.MessageSocketChannel;
import com.NA.social.core.entity.Chat;
import com.NA.social.core.entity.Message;
import com.NA.social.core.entity.MessageReact;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.MessageStatus;
import com.NA.social.core.repository.*;
import com.NA.social.core.request.chat.*;
import com.NA.social.core.ultis.ApiResponse;
import com.NA.social.core.ultis.Responser;
import com.NA.social.core.ultis.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageReactRepository messageReactRepository;

    @Override
    public ResponseEntity<ApiResponse> createMessage(SendMessageRequest request) throws Exception {
        return Responser.success(createAndGetMessage(request));
    }

    @Override
    public Message createAndGetMessage(SendMessageRequest request) throws Exception {
        User user = userRepository.findById(request.getSenderId()).orElseThrow();
        Chat chat = chatRepository.findById(request.getChatId()).orElseThrow();
        Message message = Message.builder().sender(user).messageStatus(MessageStatus.SENT).messageType(request.getMessageType()).media(new HashSet<>(mediaRepository.findAllById(request.getMediaId()))).content(request.getContent()).replyTo(userRepository.findById(request.getUserReplyId()).orElse(null)).chat(chat).build();
        return messageRepository.save(message);
    }

    @Override
    public ResponseEntity<ApiResponse> editMessage(EditMessageRequest request, long messageId) throws Exception {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setContent(request.getContent());
        return Responser.success(messageRepository.save(message));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteMessage(long messageId) throws Exception {
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        Message message = messageRepository.findById(messageId).orElseThrow();
        if (user == null || (!user.getUid().equals(message.getSender().getUid()))) return Responser.unAuth();
        messageRepository.deleteById(messageId);
        return Responser.success();
    }

    @Override
    public ResponseEntity<ApiResponse> shareMessage(ShareMessageRequest request) throws Exception {
        Message shadowMessage = new Message();
        Message originMessage = messageRepository.findById(request.getMessageId()).orElseThrow();
        Chat chat = chatRepository.findById(request.getTargetChatId()).orElseThrow();
        BeanUtils.copyProperties(originMessage, shadowMessage);
        shadowMessage.setSender(SecurityHelper.getAccountFromLogged(userRepository));
        shadowMessage.setMessageReacts(new HashSet<>());
        shadowMessage.setMessageStatus(MessageStatus.SENT);
        shadowMessage.setRedirected(true);
        shadowMessage.setChat(chat);
        Message savedMessage = messageRepository.save(shadowMessage);
        // send notification to chat
        simpMessagingTemplate.convertAndSend(MessageSocketChannel.CHAT_PATH + chat.getId(), savedMessage);
        return Responser.success(savedMessage);
    }

    @Override
    public ResponseEntity<ApiResponse> getLatestMessage(Pageable pageable, long chatId) throws Exception {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return Responser.success(messageRepository.findAllByChatOrderByCreatedAtDesc(pageable, chat));
    }

    @Override
    public ResponseEntity<ApiResponse> getUnReadMessages(Pageable pageable, long chatId) throws Exception {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return Responser.success(messageRepository.findAllByChatAndMessageStatusOrderByCreatedAtDesc(pageable, chat, MessageStatus.SENT));
    }

    @Override
    public ResponseEntity<ApiResponse> markMessageAsRead(long messageId) throws Exception {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setMessageStatus(MessageStatus.READ);
        return Responser.success(messageRepository.save(message));
    }

    @Override
    public ResponseEntity<ApiResponse> recallMessage(long messageId) throws Exception {
        Message message = messageRepository.findById(messageId).orElseThrow();
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        if (user == null || (!user.getUid().equals(message.getSender().getUid()))) return Responser.unAuth();
        message.setMessageStatus(MessageStatus.RECALL);
        message.setContent("This message has been removed");
        return Responser.success(messageRepository.save(message));
    }

    @Override
    public ResponseEntity<ApiResponse> searchMessage(long chatId, String query, Pageable pageable) throws Exception {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return Responser.success(messageRepository.findAllByChatAndContentContainingOrderByCreatedAtDesc(pageable, chat, query));

    }

    @Override
    public ResponseEntity<ApiResponse> getMessagesBetweenTime(long chatId, Instant timeStart, Instant timeEnd, Pageable pageable) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        return Responser.success(messageRepository.findAllByChatAndCreatedAtBetween(pageable, chat, timeStart, timeEnd));
    }

    @Override
    public void saveAllMessages(List<ChatMessageEvent> events) {
        try {
            List<Message> messages = events.stream()
                    .map(this::createMessageEntity)
                    .collect(Collectors.toList());
            messageRepository.saveAll(messages);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Message createMessageEntity(ChatMessageEvent request) {
        User user = userRepository.findById(request.getMessage().getSenderId()).orElseThrow();
        Chat chat = chatRepository.findById(request.getChatId()).orElseThrow();
        return Message.builder().sender(user).messageStatus(MessageStatus.SENT).messageType(request.getMessage().getMessageType()).media(new HashSet<>(mediaRepository.findAllById(request.getMessage().getMediaId()))).content(request.getMessage().getContent()).replyTo(userRepository.findById(request.getMessage().getUserReplyId()).orElse(null)).chat(chat).build();
    }


    @Override
    public ResponseEntity<ApiResponse> reactToMessage(ReactionMessageRequest request) {
        Message message = messageRepository.findById(request.getMessageId()).orElseThrow();
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        MessageReact newReact = MessageReact.builder().message(message).user(SecurityHelper.getAccountFromLogged(userRepository)).build();
        if (user == null) return Responser.unAuth();
        MessageReact messageReact = messageReactRepository.findByUserAndMessage(user, message).orElse(null);
        if (messageReact != null) {
            message.removeReact(messageReact);
        }
        message.addReact(messageReactRepository.save(newReact));
        return Responser.success(messageRepository.save(message));
    }
}
