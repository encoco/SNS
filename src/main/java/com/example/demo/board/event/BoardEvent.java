package com.example.demo.board.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardEvent extends ApplicationEvent {
    private final int recipientId;  // 나
    private final int senderId;     // 상대
    private final int boardId;

    public BoardEvent(Object source, int senderId, int recipientId, int boardId) {
        super(source);
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.boardId = boardId;
    }
}
