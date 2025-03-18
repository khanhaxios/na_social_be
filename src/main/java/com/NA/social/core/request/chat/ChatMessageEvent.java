package com.NA.social.core.request.chat;

import com.NA.social.core.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEvent {
    private long chatId;
    private SendMessageRequest message;
}
