package com.NA.social.core.request.feed;

import com.NA.social.core.enums.FeedPrivacy;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CreateFeedRequest {
    private String caption;
    private List<Long> mediaIds = new ArrayList<>();
    private FeedPrivacy privacy;
}
