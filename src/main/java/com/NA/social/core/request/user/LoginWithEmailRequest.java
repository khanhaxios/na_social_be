package com.NA.social.core.request.user;

import lombok.Data;

@Data
public class LoginWithEmailRequest {
    private String email;
    private String password;
}
