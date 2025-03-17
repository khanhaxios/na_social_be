package com.NA.social.core.request.story;

import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FeedPrivacy;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateStoryRequest {
    @NotBlank
    private String caption;
    @NotBlank
    private long mediaId;
    private Set<String> userTagIds = new HashSet<>();
    private FeedPrivacy privacy = FeedPrivacy.ONLY_FRIEND;
}
