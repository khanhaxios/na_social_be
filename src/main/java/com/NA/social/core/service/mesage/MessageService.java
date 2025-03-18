package com.NA.social.core.service.mesage;

import com.NA.social.core.entity.Message;
import com.NA.social.core.request.chat.*;
import com.NA.social.core.ultis.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

public interface MessageService {
    ResponseEntity<ApiResponse> createMessage(SendMessageRequest request) throws Exception;

    Message createAndGetMessage(SendMessageRequest request) throws Exception;


    ResponseEntity<ApiResponse> editMessage(EditMessageRequest request, long messageId) throws Exception;

    ResponseEntity<ApiResponse> deleteMessage(long messageId) throws Exception;

    ResponseEntity<ApiResponse> shareMessage(ShareMessageRequest request) throws Exception;

    ResponseEntity<ApiResponse> getLatestMessage(Pageable pageable, long chatId) throws Exception;

    ResponseEntity<ApiResponse> getUnReadMessages(Pageable pageable, long chatId) throws Exception;

    ResponseEntity<ApiResponse> markMessageAsRead(long messageId) throws Exception;

    ResponseEntity<ApiResponse> recallMessage(long messageId) throws Exception;

    ResponseEntity<ApiResponse> searchMessage(long chatId, String query, Pageable pageable) throws Exception;

    ResponseEntity<ApiResponse> getMessagesBetweenTime(long chatId, Instant timeStart, Instant timeEnd, Pageable pageable);

    public void saveAllMessages(List<ChatMessageEvent> events);

    public Message createMessageEntity(ChatMessageEvent event) throws Exception;


    ResponseEntity<ApiResponse> reactToMessage(ReactionMessageRequest request);
}
