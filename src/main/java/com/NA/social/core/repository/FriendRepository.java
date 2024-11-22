package com.NA.social.core.repository;

import com.NA.social.core.entity.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("Select f from Friend f where  f.receiver.uid=:userId or f.sender.uid = :userId")
    Page<Friend> findAllMyFriend(Pageable pageable, String userId);
    @Query("SELECT f FROM Friend f WHERE f.receiver.uid LIKE %:userId% OR f.sender.uid LIKE %:userId%")
    Page<Friend> findAllMyFriendWithLike(Pageable pageable, String userId);
}
