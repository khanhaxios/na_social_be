package com.NA.social.core.api;

import com.NA.social.core.request.story.CreateStoryRequest;
import com.NA.social.core.request.story.UpdateStoryRequest;
import com.NA.social.core.service.story.StoryService;
import com.NA.social.core.ultis.Responser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/stories")
@RequiredArgsConstructor
@RestController
@CrossOrigin
public class StoryApi {

    private final StoryService storyService;

    @GetMapping
    public ResponseEntity<?> getNewStory(Pageable pageable) {
        try {
            return storyService.getNewStory(pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/my-stories")
    public ResponseEntity<?> getMyStory(Pageable pageable) {
        try {
            return storyService.getMyStories(pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/by-author/{id}")
    public ResponseEntity<?> getStoryById(@PathVariable(name = "id") String Id, Pageable pageable) {
        try {
            return storyService.getStoriesByAuthor(pageable, Id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewStory(@Valid @RequestBody CreateStoryRequest request) {
        try {
            return storyService.createStory(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editStory(@Valid @RequestBody UpdateStoryRequest request, @PathVariable(name = "id") long Id) {
        try {
            return storyService.updateStory(request, Id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStory(@PathVariable(name = "id") long Id) {
        try {
            return storyService.deleteStory(Id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }
}
