package com.NA.social.core.request.chat;

import com.NA.social.core.enums.FeedReact;
import lombok.Data;

@Data
public class ReactionMessageRequest {
    private long messageId;
    private FeedReact feedReact;
}
