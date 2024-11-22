package com.NA.social.core.request.user;

import com.NA.social.core.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotNull
    private String uid;

    @NotNull
    private String username;
    private String password;

    private String displayName;
    private String avatar;

    private String coverUrl;
    private String phoneNumber;
    private String token;
    private RoleType roleType;
}
