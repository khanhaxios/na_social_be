package com.NA.social.core.repository;

import com.NA.social.core.entity.Notification;
import com.NA.social.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByOwner(Pageable pageable, User owner);
}
