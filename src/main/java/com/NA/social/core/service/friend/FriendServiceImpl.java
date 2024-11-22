package com.NA.social.core.service.friend;

import com.NA.social.core.conts.UserCons;
import com.NA.social.core.entity.Friend;
import com.NA.social.core.entity.Notification;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FriendStatus;
import com.NA.social.core.enums.NotificationType;
import com.NA.social.core.repository.FriendRepository;
import com.NA.social.core.repository.NotificationRepository;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.friend.ResponseAddFriendRequest;
import com.NA.social.core.ultis.NotificationHelper;
import com.NA.social.core.ultis.Responser;
import com.NA.social.core.ultis.SecurityHelper;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final NotificationHelper notificationHelper;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<?> createAddFriendRequest(String userId) {
        Friend friend = new Friend();
        User receiver = userRepository.findById(userId).orElse(null);
        User sender = SecurityHelper.getAccountFromLogged(userRepository);
        if (sender == null) return Responser.notFound();
        if (receiver == null) return Responser.notFound();
        friend.setReceiver(receiver);
        friend.setSender(sender);
        friend.setStatus(FriendStatus.WAITING_ACCEPT);
        friendRepository.save(friend);
        Friend aceptF = new Friend();
        BeanUtils.copyProperties(friend, aceptF);
        aceptF.setSender(receiver);
        aceptF.setReceiver(sender);
        friendRepository.save(aceptF);
        // send notification for user
        Notification receiverNotification = createNotification(receiver, String.format(UserCons.messageAddNewFriend, sender.getUsername()), sender);
        notificationHelper.sendNotificationToUser(receiver.getUsername(), "/friend", receiverNotification);
        return Responser.success();
    }

    private Notification createNotification(User owner, String content, Object data) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setData(new Gson().toJson(data));
        notification.setType(NotificationType.FRIEND);
        notification.setOwner(owner);
        return notificationRepository.save(notification);
    }

    @Override
    public ResponseEntity<?> responseAddFriendRequest(ResponseAddFriendRequest request) {
        Friend friend = friendRepository.findById(request.getRequestId()).orElse(null);
        if (friend == null) {
            return Responser.notFound();
        }
        friend.setStatus(request.isAccept() ? FriendStatus.BE_FRIEND : FriendStatus.DECLINE);
        // notification to users
        Friend savedFriend = friendRepository.save(friend);
        Notification senderNoti = createNotification(friend.getSender(), String.format("%s %s be your friend", friend.getReceiver().getUsername(), request.isAccept() ? "accepted" : "denied"), friend);
        Notification receiverNotification = createNotification(friend.getReceiver(), String.format("You are %s friend with %s", request.isAccept() ? "be a" : "not be a", friend.getSender().getUsername()), friend);
        notificationHelper.sendNotificationToUser(friend.getReceiver().getUsername(), "/friend", receiverNotification);
        notificationHelper.sendNotificationToUser(friend.getSender().getUsername(), "/friend", senderNoti);
        return Responser.success(savedFriend);
    }

    @Override
    public ResponseEntity<?> deleteFriend(Long receiverId) {
        Friend friend = friendRepository.findById(receiverId).orElse(null);
        if (friend == null) {
            return Responser.notFound();
        }
        friendRepository.delete(friend);
        return Responser.success();
    }

    @Override
    public ResponseEntity<?> getListFriend(Pageable pageable) {
        User currentUser = SecurityHelper.getAccountFromLogged(userRepository);
        if (currentUser == null) {
            return Responser.unAuth();
        }
        return Responser.success(friendRepository.findAllMyFriend(pageable, currentUser.getUid()));
    }

    @Override
    public ResponseEntity<?> searchFriend(Pageable pageable, String username) {
        return Responser.success(friendRepository.findAllMyFriendWithLike(pageable, username));
    }
}
