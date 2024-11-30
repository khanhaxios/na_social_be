package com.NA.social.core.repository;

import com.NA.social.core.entity.Friend;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("Select f from Friend f where  (f.receiver.uid=:userId or f.sender.uid = :userId) and f.status =:status")
    Page<Friend> findAllMyFriend(Pageable pageable, String userId, FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE f.receiver.uid LIKE %:userId% OR f.sender.uid LIKE %:userId%")
    Page<Friend> findAllMyFriendWithLike(Pageable pageable, String userId);

    @Query("SELECT f from Friend f WHERE f.receiver.uid = :id and f.status = :status")
    Page<Friend> findAllByReceiverAndStatus(Pageable pageable, @Param("id") String id, @Param("status") FriendStatus status);
}
