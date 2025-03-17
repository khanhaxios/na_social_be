package com.NA.social.core.request.story;

import com.NA.social.core.enums.FeedPrivacy;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UpdateStoryRequest {
    private String caption;
    private long mediaId;
    private Set<String> tagIds = new HashSet<>();
    private FeedPrivacy privacy;

}
