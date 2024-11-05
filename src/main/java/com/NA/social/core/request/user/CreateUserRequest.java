package com.NA.social.core.request.user;

import com.NA.social.core.enums.RoleType;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String uid;
    private String username;
    private String password;
    private String displayName;
    private String avatar;

    private String coverUrl;
    private String phoneNumber;
    private String token;
    private RoleType roleType;
}
