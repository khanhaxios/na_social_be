package com.NA.social.core.api;

import com.NA.social.core.request.friend.ResponseAddFriendRequest;
import com.NA.social.core.service.friend.FriendService;
import com.NA.social.core.ultis.Responser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class FriendApi {

    private final FriendService friendService;

    @PostMapping("/send-add-friend-request/{id}")
    ResponseEntity<?> sendAddFriendRequest(@PathVariable(name = "id") String userId) {
        try {
            return friendService.createAddFriendRequest(userId);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @PostMapping("/response-add-friend-request")
    ResponseEntity<?> responseAddFriendRequest(@RequestBody ResponseAddFriendRequest request) {
        try {
            return friendService.responseAddFriendRequest(request);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/my-friend")
    ResponseEntity<?> getMyFriend(Pageable pageable) {
        try {
            return friendService.getListFriend(pageable);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/search-friend")
    ResponseEntity<?> searchMyFriend(Pageable pageable, @RequestParam(name = "query") String query) {
        try {
            return friendService.searchFriend(pageable, query);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFriend(@PathVariable(name = "id") Long id) {
        try {
            return friendService.deleteFriend(id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

}
