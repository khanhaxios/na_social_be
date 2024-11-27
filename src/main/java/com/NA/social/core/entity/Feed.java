package com.NA.social.core.entity;

import com.NA.social.core.enums.FeedPrivacy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String caption;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    private FeedPrivacy privacy;

    @OneToMany
    private Set<Media> media = new HashSet<>();

    @OneToMany
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @OneToMany
    private Set<UserReact> userReacted = new HashSet<>();

    private boolean showing = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addComment(Comment savedComment) {
        this.comments.add(savedComment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void addOrRemoveReaction(UserReact userReact) {
        if (this.userReacted.contains(userReact)) {
            this.userReacted.remove(userReact);
        } else {
            this.userReacted.add(userReact);
        }
    }
}
