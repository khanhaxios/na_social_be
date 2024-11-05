package com.NA.social.core.service.user;

import com.NA.social.core.entity.User;
import com.NA.social.core.enums.RoleType;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.request.user.UpdateUserProfileRequest;
import com.NA.social.core.service.jwt.JwtService;
import com.NA.social.core.ultis.ApiResponse;
import com.NA.social.core.ultis.Responser;
import com.NA.social.core.ultis.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public ResponseEntity<ApiResponse> getCurrentUserInfo() {
        return Responser.success(SecurityHelper.getAccountFromLogged(userRepository));
    }

    @Override
    public ResponseEntity<ApiResponse> createOrGetUser(CreateUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            user = new User();
            BeanUtils.copyProperties(request, user);
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        }
        user.setRoleType(RoleType.ROLE_USER);
        user.setSessionToken(request.getToken());
        user.setApiToken(jwtService.signToken(user));
        return Responser.success(userRepository.save(user));
    }

    @Override
    public ResponseEntity<ApiResponse> updateProfile(UpdateUserProfileRequest request) {
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        if (user == null) {
            return Responser.badRequest(List.of("User not found"));
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        return Responser.success(userRepository.save(user));
    }
}
