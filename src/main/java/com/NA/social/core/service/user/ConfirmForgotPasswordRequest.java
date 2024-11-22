package com.NA.social.core.service.user;

import lombok.Data;

@Data
public class ConfirmForgotPasswordRequest {
    private String email;
    private String code;
}
