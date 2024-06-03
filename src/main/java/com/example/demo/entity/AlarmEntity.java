package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.DTO.AlarmDTO;
import com.example.demo.DTO.BoardLikeDTO;
import com.example.demo.DTO.CCMDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name = "alarm")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int alarm_id;

    private int recipientId;
    
    @ManyToOne
    @JoinColumn(name = "sender_id") // 외래 키 지정
	private UsersEntity sender;
	
	private int board_id;
	private String content;
	@Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	
	
	public static AlarmEntity toEntity(AlarmDTO dto) {
        return AlarmEntity.builder()
            .alarm_id(dto.getAlarm_id())
            .recipientId(dto.getRecipient_id()) 
            .sender(UsersEntity.builder().id(dto.getRecipient_id()).build())
            .board_id(dto.getBoard_id())
            .content(dto.getContent())
            .date(dto.getDate())
            .build();
	}
	
}
