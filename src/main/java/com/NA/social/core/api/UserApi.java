package com.NA.social.core.api;

import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.request.user.UpdateUserProfileRequest;
import com.NA.social.core.service.user.UserService;
import com.NA.social.core.ultis.Responser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}
