package com.example.demo.entity;

import com.example.demo.DTO.ChatDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@Entity
@Table(name = "userchat")
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userchat_id")
	private int  userchatId;
	
	@Column(name="room_number")
	private String roomNumber;
	
	@Column(name = "id")
    private int userId;
    
	private String nickname;
    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    
    public static ChatEntity toEntity(ChatDTO dto) {
        return ChatEntity.builder()
                .userchatId(dto.getUserchatId())
                .roomNumber(dto.getRoomNumber())
                .userId(dto.getId())
                .nickname(dto.getNickname())
                .date(dto.getDate())
                .build();
    }
}
