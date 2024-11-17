package com.NA.social.core.api;

import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.service.user.UserService;
import com.NA.social.core.ultis.Responser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/auth")
@CrossOrigin
public class AuthApi {
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody CreateUserRequest request) {
        try {
            return userService.createOrGetUser(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }
}
