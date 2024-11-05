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
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
        return null;
    }

    @Override
    public ResponseEntity<?> deleteFriend(Long receiverId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getListFriend(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<?> searchFriend(Pageable pageable) {
        return null;
    }
}
