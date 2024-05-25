package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.ChatMessageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
	private int message_id;
	private String room_number;
	private int id;
	private String content;
	private String nickname;
    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static ChatMessageDTO toDTO(ChatMessageEntity entity) {
        return ChatMessageDTO.builder()
        		.message_id(entity.getMessage_id())
                .room_number(entity.getRoomNumber())
                .id(entity.getId())
                .nickname(entity.getNickname())
                .content(entity.getContent())
        		.date(entity.getDate())
                .build();
    }

    public static List<ChatMessageDTO> ToDtoList(List<ChatMessageEntity> entities) {
	    List<ChatMessageDTO> dtos = new ArrayList<>();
	    for (ChatMessageEntity entity : entities) {
	        ChatMessageDTO dto = new ChatMessageDTO();
	        dto.setMessage_id(entity.getMessage_id());
	        dto.setRoom_number(entity.getRoomNumber());
	        dto.setNickname(entity.getNickname());
	        dto.setId(entity.getId());
	        dto.setContent(entity.getContent());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
