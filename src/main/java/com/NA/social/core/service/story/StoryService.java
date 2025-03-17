package com.NA.social.core.service.story;

import com.NA.social.core.request.story.CreateStoryRequest;
import com.NA.social.core.request.story.UpdateStoryRequest;
import com.NA.social.core.ultis.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface StoryService {
    public ResponseEntity<ApiResponse> createStory(CreateStoryRequest request);

    public ResponseEntity<ApiResponse> updateStory(UpdateStoryRequest request, long id);

    public ResponseEntity<ApiResponse> deleteStory(long Id);

    public ResponseEntity<ApiResponse> reactStory(long story);

    public ResponseEntity<ApiResponse> getNewStory(Pageable pageable);

    public ResponseEntity<ApiResponse> getMyStories(Pageable pageable);

    public ResponseEntity<ApiResponse> getStoriesByAuthor(Pageable pageable, String authorId);
}
