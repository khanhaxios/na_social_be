package com.NA.social.core.repository;

import com.NA.social.core.entity.Feed;
import com.NA.social.core.entity.User;
import com.NA.social.core.entity.UserReact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReactRepository extends JpaRepository<UserReact, Long> {
    boolean existsByUserAndFeed(User user, Feed feed);
    void deleteByUserAndFeed(User user,Feed feed);
}
