package com.example.demo.follow.service;

import com.example.demo.follow.dto.FollowDTO;
import com.example.demo.follow.entity.FollowEntity;
import com.example.demo.follow.event.FollowEvent;
import com.example.demo.follow.event.FollowEventListener;
import com.example.demo.follow.repository.FollowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final FollowEventListener eventPublisher;

    @Transactional
    public String followUser(int myId, int userId) {
        FollowEntity entity = followRepository.findByFollowerIdAndFollowingId(myId, userId);
        if (entity != null) {
            followRepository.delete(entity);
            return "del";
        } else {
            FollowDTO dto = new FollowDTO();
            dto.setFollowerId(myId);
            dto.setFollowingId(userId);
            entity = FollowEntity.toEntity(dto);
            followRepository.save(entity);

            //알림 이벤트 처리.
            FollowEvent event = new FollowEvent(this, myId, userId);
            eventPublisher.publishEvent(event);
            return "add";
        }
    }
}
