package com.example.demo.follow.entity;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FollowEvent extends ApplicationEvent {
    private final int recipientId;  // 나
    private final int senderId;     // 상대

    public FollowEvent(Object source, int senderId, int recipientId) {
        super(source);
        this.recipientId = recipientId;
        this.senderId = senderId;
    }
}