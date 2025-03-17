package com.NA.social.core.request.feed;

import com.NA.social.core.enums.FeedReact;
import lombok.Data;

@Data
public class ReactFeedRequest {
    private long feedId;
    private FeedReact feedReact = FeedReact.LIKE;
}
