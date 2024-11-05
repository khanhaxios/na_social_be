package com.NA.social.core.request.feed;

import com.NA.social.core.enums.FeedPrivacy;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateFeedRequest {
    private String caption;
    private Set<Long> mediaIds = new HashSet<>();
    private FeedPrivacy privacy;
}
