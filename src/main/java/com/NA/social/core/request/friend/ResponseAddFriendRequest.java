package com.NA.social.core.request.friend;

import lombok.Data;

@Data
public class ResponseAddFriendRequest {
    private Long requestId;
    private boolean isAccept;
}
