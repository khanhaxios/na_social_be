package com.NA.social.core.repository;

import com.NA.social.core.entity.Chat;
import com.NA.social.core.entity.Message;
import com.NA.social.core.enums.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByChatOrderByCreatedAtDesc(Pageable pageable, Chat chat);

    Page<Message> findAllByChatAndMessageStatusOrderByCreatedAtDesc(Pageable pageable, Chat chat, MessageStatus messageStatus);

    Page<Message> findAllByChatAndContentContainingOrderByCreatedAtDesc(Pageable pageable, Chat chat, String content);

    Page<Message> findAllByChatAndCreatedAtBetween(
            Pageable pageable,
            Chat chat, Instant timeStart, Instant timeEnd
    );
}
