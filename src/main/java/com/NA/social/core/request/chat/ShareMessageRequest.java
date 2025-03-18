package com.NA.social.core.request.chat;

import lombok.Data;

@Data
public class ShareMessageRequest {
    private long messageId;
    private long targetChatId;
}
