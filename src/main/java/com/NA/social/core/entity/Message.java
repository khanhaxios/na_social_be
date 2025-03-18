package com.NA.social.core.entity;

import com.NA.social.core.enums.MessageStatus;
import com.NA.social.core.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @ManyToOne
    private Chat chat;
    @ManyToOne
    private User sender;
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus = MessageStatus.SENT;

    @OneToMany
    private Set<MessageReact> messageReacts = new HashSet<>();
    @ManyToOne
    private User replyTo;
    private boolean redirected = false;

    @OneToMany
    private Set<Media> media = new HashSet<>();

    private MessageType messageType = MessageType.TEXT;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    public void addReact(MessageReact messageReact){
        this.messageReacts.add(messageReact);
    }
    public void removeReact(MessageReact react){
        this.messageReacts.remove(react);
    }
}
