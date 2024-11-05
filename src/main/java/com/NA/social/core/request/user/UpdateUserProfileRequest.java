package com.NA.social.core.request.user;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String avatar;
    private String phoneNumber;
    private String displayName;
}
