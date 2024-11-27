package com.NA.social.core.service.user;

import com.NA.social.core.entity.User;
import com.NA.social.core.enums.RoleType;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.user.CreateUserRequest;
import com.NA.social.core.request.user.ResgiterRequest;
import com.NA.social.core.request.user.UpdateUserProfileRequest;
import com.NA.social.core.service.jwt.JwtService;
import com.NA.social.core.service.mail.MailService;
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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final MailService mailService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<ApiResponse> getCurrentUserInfo() {
        return Responser.success(SecurityHelper.getAccountFromLogged(userRepository));
    }

    @Override
    public ResponseEntity<ApiResponse> createOrGetUser(CreateUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        // case reg new user
        if (user == null) {
            user = new User();
            BeanUtils.copyProperties(request, user);
            if (request.getPassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            }
            user.setUid(request.getUid());
        }

        if (request.getPassword() != null && user.getPassword() != null) {
            if (!new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
                List<String> message = new ArrayList<>();
                message.add("Password incorrect");
                return Responser.badRequest(message);
            }
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

    @Override
    public ResponseEntity<?> newUser(ResgiterRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setRoleType(RoleType.ROLE_USER);
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setAvatar(request.getAvatar());
        user.setApiToken(jwtService.signToken(user));
        return Responser.success(userRepository.save(user));
    }

    @Override
    public ResponseEntity<?> sendForgotPasswordRequest(String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null) {
            return Responser.notFound();
        }
        String code = SecurityHelper.generateRandomNumberString(6);
        String message = String.format("Confirm forgot password code : %s ", code);
        mailService.sendMailAsync(email, message, "Confirm your forgot password request");
        user.setVerifyCode(code);
        user.setVerifyExpired(Instant.now().plus(Duration.ofMinutes(15)));
        userRepository.save(user);
        return Responser.success();
    }

    @Override
    public ResponseEntity<?> confirmForgotPassword(String code, String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null) {
            return Responser.notFound();
        }
        if (Instant.now().isBefore(user.getVerifyExpired())) {
            List<String> message = new ArrayList<>();
            message.add("Expired Time");
            return Responser.badRequest(message);
        }
        String newPassword = new BCryptPasswordEncoder().encode(SecurityHelper.generateRandomNumberString(12));
        user.setPassword(newPassword);
        // send mail
        userRepository.save(user);
        mailService.sendMailAsync(email, String.format("New password : %s", newPassword), "Your new password");
        return Responser.success();
    }

    @Override
    public ResponseEntity<?> loginWithEmailAndPassword(String email, String password) {
        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null) {
            return Responser.notFound();
        }
        if (password != null && user.getPassword() != null) {
            if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                List<String> message = new ArrayList<>();
                message.add("Password incorrect");
                return Responser.badRequest(message);
            }
        }
        user.setApiToken(jwtService.signToken(user));
        return Responser.success(user);
    }
}
