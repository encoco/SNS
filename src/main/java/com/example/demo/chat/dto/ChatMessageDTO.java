package com.example.demo.chat.dto;

import com.example.demo.chat.entity.ChatMessageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "일반채팅 메시지 DTO" )
public class ChatMessageDTO {
    @Schema(description = "일반채팅 메세지 ID" , example = "1")
    private int message_id;

    @Schema(description = "채팅방 넘버" , example = "5f203f1c-8dab-4e35-a0e8-1461253cc68c")
    private String room_number;

    @Schema(description = "유저 ID" , example = "1")
    private int id;

    @Schema(description = "채팅 메세지 내용" , example = "ㅎㅇㅎㅇ")
    private String content;

    @Schema(description = "닉네임" , example = "nick")
    private String nickname;

    @Schema(description = "프로필 사진" , example = "프로필사진 주소")
    private String profile_img;

    @Schema(description = "채팅방에 공유한 게시물 ID" , example = "766")
    private Integer share_board_id;
    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static ChatMessageDTO toDTO(ChatMessageEntity entity) {
        return ChatMessageDTO.builder()
                .message_id(entity.getMessage_id())
                .room_number(entity.getRoomNumber())
                .id(entity.getId())
                .profile_img(entity.getProfile_img())
                .share_board_id(entity.getShare_board_id())
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
            dto.setShare_board_id(entity.getShare_board_id());
            dto.setProfile_img(entity.getProfile_img());
            dto.setId(entity.getId());
            dto.setContent(entity.getContent());
            dto.setDate(entity.getDate());
            dtos.add(dto);
        }
        return dtos;
    }
}
