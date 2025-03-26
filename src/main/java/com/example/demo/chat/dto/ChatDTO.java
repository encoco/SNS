package com.example.demo.chat.dto;

import com.example.demo.chat.entity.ChatEntity;
import com.example.demo.chat.entity.ChatParticipantEntity;
import com.example.demo.user.entity.UsersEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 DTO" )
public class ChatDTO {
    @Schema(description = "채팅방 ID" , example = "1")
    private int userchatId;

    @Schema(description = "유저 ID" , example = "1")
    private int id;

    @Schema(description = "채팅방 이름(참여자 이름 조합으로 채팅 목록에 뜨게 함)" , example = "류경록/홍길동/김첨지")
    private String roomname;

    @Schema(description = "참여자 프로필 사진" , example = "이미지 주소~~")
    private String profile_img;

    @Schema(description = "새로 만든 채팅방인지 생성할 때 확인용" , example = "T/F")
    private boolean new_room;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    public static ChatDTO toDTO(ChatEntity entity, int myId) {
        List<ChatParticipantEntity> participants = entity.getParticipants();

        String roomName = (participants != null)
                ? participants.stream()
                .map(ChatParticipantEntity::getUser)
                .filter(u -> u.getId() != myId)
                .map(UsersEntity::getNickname)
                .collect(Collectors.joining("/"))
                : "";

        return ChatDTO.builder()
                .userchatId(entity.getId())
                .id(myId)
                .roomname(roomName)
                .date(entity.getDate())
                .build();
    }
}
