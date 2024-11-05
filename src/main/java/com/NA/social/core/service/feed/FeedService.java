package com.NA.social.core.service.feed;

import com.NA.social.core.request.feed.CreateCommentFeedRequest;
import com.NA.social.core.request.feed.CreateFeedRequest;
import com.NA.social.core.request.feed.UpdateFeedRequest;
import com.NA.social.core.ultis.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FeedService {

    ResponseEntity<ApiResponse> getNewsFeed(Pageable pageable);

    ResponseEntity<ApiResponse> getMyFeed(Pageable pageable);

    ResponseEntity<ApiResponse> getUserFeed(Pageable pageable, String userId);

    ResponseEntity<ApiResponse> createNewFeed(CreateFeedRequest request);

    ResponseEntity<ApiResponse> updateFeed(Long feedId, UpdateFeedRequest request);

    ResponseEntity<ApiResponse> deleteFeed(Long feedId);

    ResponseEntity<ApiResponse> commentFeed(CreateCommentFeedRequest request);

    ResponseEntity<ApiResponse> reactFeed(Long feedId);
}
