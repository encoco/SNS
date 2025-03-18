package com.example.demo.alarm.service;

import com.example.demo.alarm.dto.AlarmDTO;
import com.example.demo.alarm.entity.AlarmEntity;
import com.example.demo.alarm.repository.AlarmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @Transactional
    public List<AlarmDTO> getAlarm(int id) {
        List<AlarmEntity> entities = alarmRepository.findByRecipientIdOrderByDateDesc(id);

        // 모든 엔티티의 read 값을 true로 변경
        entities.forEach(entity -> entity.setIsread(true));

        // 변경된 엔티티들을 저장
        alarmRepository.saveAll(entities);

        return AlarmDTO.toDtoList(entities);
    }

    public boolean getCheckAlarm(int id) {
        if (alarmRepository.existsByRecipientIdAndReadFalse(id) > 0) return true;
        else return false;
    }

    public void delAllAlarm(int id) {
        List<AlarmEntity> entity = alarmRepository.findByRecipientId(id);
        alarmRepository.deleteAll(entity);
    }
}
