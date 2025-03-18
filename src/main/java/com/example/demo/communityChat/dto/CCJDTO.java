package com.example.demo.communityChat.dto;

import com.example.demo.communityChat.entity.CommunityChatJoinEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "오픈채팅 참여자를 위한 DTO")
public class CCJDTO {
    @Schema(description = "참여자 PKey", example = "123")
    private int ccjId;

    @Schema(description = "방 번호 FK(communityChat PKey)", example = "12")
    private int roomNumber;

    @Schema(description = "방장이 입력한 방 이름", example = "수다떨사람?")
    private String roomname;

    @Schema(description = "참여자 ID", example = " 1")
    private int id;

    @Schema(description = "방목록에 보여질 이미지", example = "www.S3오픈채팅배경사진.jpg")
    private String imgpath;

    @Schema(description = "방 생성 날짜", example = "2025/03/18 16:15")
    private String date;


    public static List<CCJDTO> toDTOList(List<CommunityChatJoinEntity> entitis) {
        List<CCJDTO> dtos = new ArrayList<>();
        for (CommunityChatJoinEntity entity : entitis) {
            CCJDTO dto = new CCJDTO();
            dto.setCcjId(entity.getCcjId());
            dto.setRoomNumber(entity.getCommunitychatId());
            dto.setId(entity.getId());
            dto.setImgpath(entity.getImgpath());
            dto.setRoomname(entity.getTitle());
            dto.setDate(entity.getDate());
            dtos.add(dto);
        }
        return dtos;
    }
}
