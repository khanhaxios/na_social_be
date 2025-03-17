package com.NA.social.core.service.story;

import com.NA.social.core.entity.Media;
import com.NA.social.core.entity.Notification;
import com.NA.social.core.entity.Story;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FriendStatus;
import com.NA.social.core.enums.NotificationType;
import com.NA.social.core.repository.MediaRepository;
import com.NA.social.core.repository.NotificationRepository;
import com.NA.social.core.repository.StoryRepository;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.story.CreateStoryRequest;
import com.NA.social.core.request.story.UpdateStoryRequest;
import com.NA.social.core.ultis.*;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final NotificationHelper notificationHelper;
    private final NotificationRepository notificationRepository;


    @Override
    public ResponseEntity<ApiResponse> createStory(CreateStoryRequest request) {
        // create story first
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        if (user == null) {
            return Responser.unAuth();
        }
        Media media = mediaRepository.findById(request.getMediaId()).orElseThrow();
        Story story = Story.builder().storyCaption(request.getCaption()).author(user).media(media).tags(new HashSet<>(userRepository.findAllById(request.getUserTagIds().stream().toList()))).privacy(request.getPrivacy()).build();
        Story savedStory = storyRepository.save(story);
        List<User> users = userRepository.findAllById(request.getUserTagIds().stream().toList());
        List<Notification> notifications = createNotifications(users, String.format("%s  vừa nhắc đến bạn trong câu chuyện của họ", user.getDisplayName()), savedStory);
        notificationHelper.sendNotificationToUsers(notifications, "/stories");
        // create all notification
        return Responser.success(savedStory);
    }

    private List<Notification> createNotifications(List<User> users, String content, Object data) {
        List<Notification> notifications = new ArrayList<>();
        for (User user : users) {
            notifications.add(createNotification(user, content, data));
        }
        return notificationRepository.saveAll(notifications);
    }

    private Notification createNotification(User owner, String content, Object data) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setData(new Gson().toJson(data));
        notification.setType(NotificationType.FRIEND);
        notification.setOwner(owner);
        return notification;
    }


    @Override
    public ResponseEntity<ApiResponse> updateStory(UpdateStoryRequest request, long id) {
        Story story = storyRepository.findById(id).orElseThrow();
        BeanUtils.copyProperties(request, story, BeanHelper.getNullPropertyNames(request));
        return Responser.success(storyRepository.save(story));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteStory(long Id) {
        Story story = storyRepository.findById(Id).orElseThrow();
        story.setShowing(false);
        storyRepository.save(story);
        return Responser.success();
    }

    @Override
    public ResponseEntity<ApiResponse> reactStory(long story) {
        storyRepository.incrementRectCount(story);
        return Responser.success();
    }

    @Override
    public ResponseEntity<ApiResponse> getNewStory(Pageable pageable) {
        //process to get friend story today
        User currentUser = SecurityHelper.getAccountFromLogged(userRepository);
        if (currentUser == null) {
            return Responser.unAuth();
        }
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Responser.success(storyRepository.getNewStories(pageable, currentUser, startOfDay, FriendStatus.BE_FRIEND));
    }

    @Override
    public ResponseEntity<ApiResponse> getMyStories(Pageable pageable) {
        User currentUser = SecurityHelper.getAccountFromLogged(userRepository);
        if (currentUser == null) {
            return Responser.unAuth();
        }
        return Responser.success(storyRepository.findAllByAuthor(pageable, currentUser));
    }

    @Override
    public ResponseEntity<ApiResponse> getStoriesByAuthor(Pageable pageable, String authorId) {
        User user = userRepository.findById(authorId).orElseThrow();
        return Responser.success(storyRepository.findAllByAuthorAndPrivacy(pageable, user));
    }
}
