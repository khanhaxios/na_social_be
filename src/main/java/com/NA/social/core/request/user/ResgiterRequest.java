package com.NA.social.core.request.user;

import lombok.Data;

@Data
public class ResgiterRequest {
    private String username;
    private String phone;
    private String password;
    private String email;

}
