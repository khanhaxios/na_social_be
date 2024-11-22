package com.NA.social.core.service.friend;

import com.NA.social.core.request.friend.ResponseAddFriendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FriendService {
    public ResponseEntity<?> createAddFriendRequest(String userId);

    public ResponseEntity<?> responseAddFriendRequest(ResponseAddFriendRequest request);

    public ResponseEntity<?> deleteFriend(Long receiverId);

    public ResponseEntity<?> getListFriend(Pageable pageable);

    public ResponseEntity<?> searchFriend(Pageable pageable, String query);
}
