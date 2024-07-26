package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.ChatEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
	private int  userchatId;
	private String roomNumber;
    private int id;
    private String roomname;
    private String content;
    private String joinId;
    private String profile_img;
    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static ChatDTO toDTO(ChatEntity entity) {
        return ChatDTO.builder()
                .userchatId(entity.getUserchatId())
                .roomNumber(entity.getRoomNumber())
                .id(entity.getId())
                .roomname(entity.getNickname())
                .joinId(entity.getJoinId())
                .date(entity.getDate())
                .build();
    }

    public static List<ChatDTO> ToDtoList(List<ChatEntity> entities) {
	    List<ChatDTO> dtos = new ArrayList<>();
	    for (ChatEntity entity : entities) {
	        ChatDTO dto = new ChatDTO();
	        dto.setUserchatId(entity.getUserchatId());
	        dto.setRoomNumber(entity.getRoomNumber());
	        dto.setId(entity.getId());
	        dto.setRoomname(entity.getNickname());
	        dto.setProfile_img(entity.getProfile_img());
	        dto.setJoinId(entity.getJoinId());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
