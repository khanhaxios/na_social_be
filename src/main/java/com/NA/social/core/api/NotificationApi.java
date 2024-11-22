package com.NA.social.core.api;

import com.NA.social.core.entity.User;
import com.NA.social.core.repository.NotificationRepository;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.ultis.Responser;
import com.NA.social.core.ultis.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@CrossOrigin
@Slf4j
public class NotificationApi {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping
    ResponseEntity<?> getAllMyNotification(Pageable pageable) {
        try {
            User currentUser = SecurityHelper.getAccountFromLogged(userRepository);
            if (currentUser == null) {
                return Responser.unAuth();
            }
            return Responser.success(notificationRepository.findAllByOwner(pageable, currentUser));
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

}
