package com.NA.social.core.request.feed;

import com.NA.social.core.enums.FeedPrivacy;
import lombok.Data;

@Data
public class ShareFeedRequest {
    private String caption;
    private FeedPrivacy privacy;
    private long feedId;
}
