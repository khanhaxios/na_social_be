package com.NA.social.core.api;

import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.request.user.UpdateUserProfileRequest;
import com.NA.social.core.service.user.ConfirmForgotPasswordRequest;
import com.NA.social.core.service.user.UserService;
import com.NA.social.core.ultis.Responser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin
public class UserApi {
    private final UserService userService;

    @GetMapping("/info")
    ResponseEntity<?> getCurrentUser() {
        try {
            return userService.getCurrentUserInfo();
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PutMapping
    ResponseEntity<?> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        try {
            return userService.updateProfile(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping("/forgot-password/{email}")
    ResponseEntity<?> ForgotPassword(@PathVariable(name = "email") String email) {
        try {
            return userService.sendForgotPasswordRequest(email);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping("/confirm-forgot-password")
    ResponseEntity<?> ForgotPassword(@RequestBody ConfirmForgotPasswordRequest request) {
        try {
            return userService.confirmForgotPassword(request.getCode(), request.getEmail());
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }
}
