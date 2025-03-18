package com.example.demo.chat.dto;

import com.example.demo.chat.entity.ChatEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "채팅방 DTO" )
public class ChatDTO {
    @Schema(description = "채팅방 ID" , example = "1")
    private int userchatId;

    @Schema(description = "방 고유키" , example = "5f203f1c-8dab-4e35-a0e8-1461253cc68c")
    private String roomNumber;

    @Schema(description = "유저 ID" , example = "1")
    private int id;

    @Schema(description = "채팅방 이름(참여자 이름 조합으로 채팅 목록에 뜨게 함)" , example = "류경록/홍길동/김첨지")
    private String roomname;

    @Schema(description = "참여자 유저 ID 조합" , example = "12,13")
    private String joinId;

    @Schema(description = "참여자 프로필 사진" , example = "이미지 주소~~")
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
