package com.NA.social.core.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResgiterRequest {
    @NotNull
    private String username;
    private String phone;
    @NotNull
    private String password;
    @NotNull
    private String email;

    @NotNull
    private String avatar;

}
