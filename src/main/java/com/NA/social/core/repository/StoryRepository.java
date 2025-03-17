package com.NA.social.core.repository;

import com.NA.social.core.entity.Story;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Page<Story> findAllByAuthor(Pageable pageable, User author);

    @Query("select st from Story st where st.author=:author and (st.privacy='ONLY_FRIEND' or st.privacy='PUBLIC')")
    Page<Story> findAllByAuthorAndPrivacy(Pageable pageable, @Param("author") User author);

    @Query("""
                SELECT s FROM Story s 
                WHERE s.author IN (
                    SELECT CASE 
                        WHEN f.sender = :user THEN f.receiver
                        ELSE f.sender 
                    END
                    FROM Friend f 
                    WHERE (f.sender = :user OR f.receiver = :user) 
                    AND f.status = :status
                )
                AND s.createdAt >= :startOfDay
            """)
    public Page<Story> getNewStories(Pageable pageable, @Param("user") User user, @Param("startOfDay") Instant startOfDay, @Param("status") FriendStatus status);

    @Modifying
    @Query("update Story st set st.storyReactCount = st.storyReactCount+1 where st.Id = :Id")
    public void incrementRectCount(@Param("Id") long Id);
}
