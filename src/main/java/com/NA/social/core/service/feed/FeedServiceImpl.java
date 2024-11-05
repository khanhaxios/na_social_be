package com.NA.social.core.service.feed;

import com.NA.social.core.entity.Feed;
import com.NA.social.core.entity.Media;
import com.NA.social.core.entity.User;
import com.NA.social.core.enums.FeedPrivacy;
import com.NA.social.core.repository.CommentRepository;
import com.NA.social.core.repository.FeedRepository;
import com.NA.social.core.repository.MediaRepository;
import com.NA.social.core.repository.UserRepository;
import com.NA.social.core.request.feed.CreateCommentFeedRequest;
import com.NA.social.core.request.feed.CreateFeedRequest;
import com.NA.social.core.request.feed.UpdateFeedRequest;
import com.NA.social.core.ultis.ApiResponse;
import com.NA.social.core.ultis.Responser;
import com.NA.social.core.ultis.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> getNewsFeed(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> getMyFeed(Pageable pageable) {
        User author = SecurityHelper.getAccountFromLogged(userRepository);
        if (author == null) {
            return Responser.notFound();
        }
        return Responser.success(feedRepository.findAllByAuthor(pageable, author));
    }

    @Override
    public ResponseEntity<ApiResponse> getUserFeed(Pageable pageable, String userId) {
        User author = userRepository.findById(userId).orElse(null);
        if (author == null) {
            return Responser.notFound();
        }
        return Responser.success(feedRepository.findByVisibilityPublicOrOnlyFriend(pageable, author.getUid(), FeedPrivacy.PUBLIC, FeedPrivacy.ONLY_FRIEND));
    }

    @Override
    public ResponseEntity<ApiResponse> createNewFeed(CreateFeedRequest request) {
        Feed newFeed = new Feed();
        newFeed.setAuthor(SecurityHelper.getAccountFromLogged(userRepository));
        newFeed.setCaption(request.getCaption());
        newFeed.setPrivacy(request.getPrivacy());
        newFeed.getMedia().addAll(mediaRepository.findAllById(request.getMediaIds()));
        return Responser.success(feedRepository.save(newFeed));
    }

    @Override
    public ResponseEntity<ApiResponse> updateFeed(Long feedId, UpdateFeedRequest request) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed == null) {
            return Responser.notFound();
        }
        BeanUtils.copyProperties(request, feed);
        return Responser.success(feedRepository.save(feed));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed != null) {
            feed.setShowing(false);
            feedRepository.save(feed);
        }
        return Responser.success();
    }

    @Override
    public ResponseEntity<ApiResponse> commentFeed(CreateCommentFeedRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> reactFeed(Long feedId) {
        return null;
    }
}
