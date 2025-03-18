package com.NA.social.core.repository;

import com.NA.social.core.entity.Chat;
import com.NA.social.core.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    @Query("select cm from ChatMember cm where cm.chat.Id = :chatId")
    List<ChatMember> findAllByChatId(@Param("chatId") long chatId);
}
