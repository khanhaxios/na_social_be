package com.NA.social.core.request.feed;

import lombok.Data;

@Data
public class CreateCommentFeedRequest {
    private String content;
    private Long feedId;
    private Long parentId;
}
