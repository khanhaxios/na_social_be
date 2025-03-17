package com.NA.social.core.entity;

import com.NA.social.core.enums.FeedPrivacy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Story {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String storyCaption;
    private int storyReactCount = 0;

    @Enumerated(EnumType.STRING)
    private FeedPrivacy privacy;
    @ManyToOne
    private User author;

    @OneToMany
    private Set<User> tags = new HashSet<>();

    @OneToOne
    private Media media;
    private boolean showing = true;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
