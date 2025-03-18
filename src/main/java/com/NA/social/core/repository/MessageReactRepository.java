package com.NA.social.core.repository;

import com.NA.social.core.entity.Message;
import com.NA.social.core.entity.MessageReact;
import com.NA.social.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageReactRepository extends JpaRepository<MessageReact, Long> {
    Optional<MessageReact> findByUserAndMessage(User user, Message message);
}
