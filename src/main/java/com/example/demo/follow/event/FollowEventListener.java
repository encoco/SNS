package com.example.demo.follow.event;

import com.example.demo.alarm.entity.AlarmEntity;
import com.example.demo.alarm.repository.AlarmRepository;
import com.example.demo.follow.entity.FollowEvent;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FollowEventListener {
    private final AlarmRepository alarmRepository;
    private final UsersRepository usersRepository;

    @EventListener
    public void publishEvent(FollowEvent event) {
        UsersEntity sender = usersRepository.getReferenceById(event.getSenderId());
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .recipientId(event.getRecipientId())
                .sender(sender)
                .content("님이 팔로우하셨습니다.")
                .isread(false)
                .build();
        alarmRepository.save(alarmEntity);
    }
}
