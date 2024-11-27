package com.NA.social.core.repository;

import com.NA.social.core.entity.Feed;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FeedPrivacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByAuthor(Pageable pageable, User author);

    @Query("SELECT f FROM Feed f WHERE f.author.uid = :user AND (f.privacy = :public OR f.privacy = :only)")
    Page<Feed> findByVisibilityPublicOrOnlyFriend(Pageable pageable, @Param("user") String userId, @Param("public") FeedPrivacy feedPrivacy, @Param("only") FeedPrivacy feedPrivacy1);

    Page<Feed> findAllByAuthorAndPrivacy(Pageable pageable, User user, FeedPrivacy privacy);

    @Query("  SELECT f \n" +
            "    FROM Feed f \n" +
            "    WHERE f.author.uid IN (\n" +
            "        SELECT CASE \n" +
            "                   WHEN fr.sender.uid = :userId THEN fr.receiver.uid \n" +
            "                   ELSE fr.sender.uid \n" +
            "               END\n" +
            "        FROM Friend fr\n" +
            "        WHERE fr.sender.uid = :userId OR fr.receiver.uid = :userId\n" +
            "    ) AND f.privacy =:privacy order by f.createdAt desc")
    Page<Feed> getNewsFeed(Pageable pageable, @Param("userId") String userId,@Param("privacy") FeedPrivacy privacy);
}
