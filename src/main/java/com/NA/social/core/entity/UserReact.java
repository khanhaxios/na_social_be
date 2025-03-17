package com.NA.social.core.entity;

import com.NA.social.core.enums.FeedReact;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReact {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @ManyToOne
    private Feed feed;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private FeedReact feedReact;

}
