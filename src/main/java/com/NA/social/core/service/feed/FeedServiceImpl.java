package com.NA.social.core.service.feed;

import com.NA.social.core.entity.*;
import com.NA.social.core.enums.FeedPrivacy;
import com.NA.social.core.repository.*;
import com.NA.social.core.request.feed.*;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final UserReactRepository userReactRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllFeedComment(long feedId, Pageable pageable) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed == null) {
            return Responser.notFound();
        }
        return Responser.success(commentRepository.findAllByFeedAndParentComment(pageable, feed, null));
    }

    @Override
    public ResponseEntity<ApiResponse> getNewsFeed(Pageable pageable) {
        User currentUser = SecurityHelper.getAccountFromLogged(userRepository);
        if (currentUser == null) {
            return Responser.unAuth();
        }
        return Responser.success(feedRepository.getNewsFeed(pageable, currentUser.getUid(), FeedPrivacy.ONLY_FRIEND));
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
        List<Media> media = mediaRepository.findAllById(request.getMediaIds());
        newFeed.getMedia().addAll(media);
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
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        if (user == null) {
            return Responser.unAuth();
        }
        if (feed == null) {
            return Responser.notFound();
        }
        if (!user.getUid().equals(feed.getAuthor().getUid())) {
            return Responser.badRequest(List.of("This Feed Not Your"));
        }
        feed.setShowing(false);
        feedRepository.save(feed);
        return Responser.success(feed);
    }

    @Override
    public ResponseEntity<ApiResponse> commentFeed(CreateCommentFeedRequest request) {
        Feed feed = feedRepository.findById(request.getFeedId()).orElse(null);
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        if (user == null) {
            return Responser.unAuth();
        }
        if (feed == null) {
            return Responser.notFound();
        }
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCommenter(user);
        comment.setParentComment(commentRepository.findById(request.getParentId()).orElse(null));
        Comment savedComment = commentRepository.save(comment);
        return Responser.success(savedComment);
    }

    @Override
    public ResponseEntity<ApiResponse> reactFeed(ReactFeedRequest request) {
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        Feed feed = feedRepository.findById(request.getFeedId()).orElse(null);

        if (user == null) {
            return Responser.unAuth();
        }
        if (feed == null) {
            return Responser.notFound();
        }

        boolean userHasReacted = userReactRepository.existsByUserAndFeed(user, feed);
        if (userHasReacted) {
            userReactRepository.deleteByUserAndFeed(user, feed);
            feed.setReactCount(feed.getReactCount() - 1);
            return Responser.success(feedRepository.save(feed));
        }
        UserReact userReact = UserReact.builder().user(user).feed(feed).feedReact(request.getFeedReact()).build();
        userReactRepository.save(userReact);
        feed.setReactCount(feed.getReactCount() + 1);
        return Responser.success(feedRepository.save(feed));
    }

    @Override
    public ResponseEntity<ApiResponse> shareFeed(ShareFeedRequest request) {
        User user = SecurityHelper.getAccountFromLogged(userRepository);
        Feed feed = feedRepository.findById(request.getFeedId()).orElse(null);
        if (user == null || feed == null) {
            return Responser.notFound();
        }
        Feed myFeed = Feed.builder()
                .caption(request.getCaption())
                .privacy(request.getPrivacy())
                .shareFrom(feed)
                .author(user)
                .media(feed.getMedia())
                .showing(true)
                .build();
        return Responser.success(feedRepository.save(myFeed));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllChildComment(long parentId, Pageable pageable) {
        Comment comment = commentRepository.findById(parentId).orElse(null);
        if (comment == null) {
            return Responser.notFound();
        }
        return Responser.success(commentRepository.findAllByParentComment(pageable, comment));
    }
}
