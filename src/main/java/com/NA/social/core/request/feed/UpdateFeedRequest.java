package com.NA.social.core.request.feed;

import com.NA.social.core.enums.FeedPrivacy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UpdateFeedRequest {
    @NotNull
    private String caption;

    private Set<Long> mediaIds = new HashSet<>();

    @NotNull
    private FeedPrivacy privacy = FeedPrivacy.PRIVATE;
}
