package com.NA.social.core.api;

import com.NA.social.core.request.feed.*;
import com.NA.social.core.service.feed.FeedService;
import com.NA.social.core.ultis.Responser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
@CrossOrigin
public class FeedApi {
    private final FeedService feedService;

    @GetMapping("/news-feed")
    public ResponseEntity<?> getNewFeed(Pageable pageable) {
        try {
            return feedService.getNewsFeed(pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/my-feed")
    public ResponseEntity<?> getMyFeed(Pageable pageable) {
        try {
            return feedService.getMyFeed(pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserFeed(Pageable pageable, @PathVariable("id") String id) {
        try {
            return feedService.getUserFeed(pageable, id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewFeed(@Valid @RequestBody CreateFeedRequest request) {
        try {
            return feedService.createNewFeed(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeed(@PathVariable("id") Long id, @RequestBody UpdateFeedRequest request) {
        try {
            return feedService.updateFeed(id, request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") Long id) {
        try {
            return feedService.deleteFeed(id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping("/comment-feed")
    public ResponseEntity<?> commentFeed(@RequestBody CreateCommentFeedRequest request) {
        try {
            return feedService.commentFeed(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PutMapping("/react-feed")
    public ResponseEntity<?> reactFeed(@Valid @RequestBody ReactFeedRequest request) {
        try {
            return feedService.reactFeed(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping("/share-feed")
    public ResponseEntity<?> shareFeed(@Valid @RequestBody ShareFeedRequest request) {
        try {
            return feedService.shareFeed(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/comments/get-root/{id}")
    public ResponseEntity<?> getRootComment(@PathVariable(name = "id") long feedId, Pageable pageable) {
        try {
            return feedService.getAllFeedComment(feedId, pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/comments/get-child/{id}")
    public ResponseEntity<?> getChildComment(@PathVariable(name = "id") long commentId, Pageable pageable) {
        try {
            return feedService.getAllChildComment(commentId, pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }
}
