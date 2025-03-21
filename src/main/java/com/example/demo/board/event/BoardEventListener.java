package com.example.demo.board.event;

import com.example.demo.alarm.entity.AlarmEntity;
import com.example.demo.alarm.repository.AlarmRepository;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardEventListener {
    private final AlarmRepository alarmRepository;
    private final UsersRepository usersRepository;

    @EventListener
    public void publishEventComment(BoardEvent event) {
        UsersEntity sender = usersRepository.getReferenceById(event.getSenderId());
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .recipientId(event.getRecipientId())
                .sender(sender)
                .content("님이 댓글을 달았습니다.")
                .isread(false)
                .build();
        alarmRepository.save(alarmEntity);
    }

    @EventListener
    public void publishEventBoardLike(BoardEvent event) {
        UsersEntity sender = usersRepository.getReferenceById(event.getSenderId());
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .recipientId(event.getRecipientId())
                .sender(sender)
                .content("님이 글을 좋아합니다.")
                .isread(false)
                .build();
        alarmRepository.save(alarmEntity);
    }

}
