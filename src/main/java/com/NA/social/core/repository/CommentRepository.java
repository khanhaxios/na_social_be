package com.NA.social.core.repository;

import com.NA.social.core.entity.Comment;
import com.NA.social.core.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByFeedAndParentComment(Pageable pageable, Feed feed,Comment parentComment);
    Page<Comment> findAllByParentComment(Pageable pageable,Comment parentComment);
}
