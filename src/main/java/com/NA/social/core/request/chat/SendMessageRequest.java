package com.NA.social.core.request.chat;

import com.NA.social.core.enums.MessageType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SendMessageRequest {
    private String content;
    private List<Long> mediaId = new ArrayList<>();

    private MessageType messageType = MessageType.TEXT;
    private String userReplyId;
    private String senderId;

    private long chatId;

}
