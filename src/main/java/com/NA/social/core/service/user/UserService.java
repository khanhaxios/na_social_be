package com.NA.social.core.service.user;

import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.request.user.ResgiterRequest;
import com.NA.social.core.request.user.UpdateUserProfileRequest;
import com.NA.social.core.ultis.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse> getCurrentUserInfo();

    ResponseEntity<ApiResponse> createOrGetUser(CreateUserRequest request);

    ResponseEntity<ApiResponse> updateProfile(UpdateUserProfileRequest request);

    ResponseEntity<?> newUser(ResgiterRequest request);

    ResponseEntity<?> sendForgotPasswordRequest(String email);

    ResponseEntity<?> confirmForgotPassword(String code, String email);

    ResponseEntity<?> loginWithEmailAndPassword(String email, String password);
}
