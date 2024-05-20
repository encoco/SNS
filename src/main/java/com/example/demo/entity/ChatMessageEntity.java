package com.example.demo.entity;

import com.example.demo.DTO.ChatMessageDTO;
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
@Table(name = "userchatmessage")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int message_id;
	private String room_number;
	private int id;
	private String content;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    
    public static ChatMessageEntity toEntity(ChatMessageDTO dto) {
        return ChatMessageEntity.builder()
        		.message_id(dto.getMessage_id())
                .room_number(dto.getRoom_number())
                .id(dto.getId())
                .content(dto.getContent())
        		.date(dto.getDate())
                .build();
    }
}
