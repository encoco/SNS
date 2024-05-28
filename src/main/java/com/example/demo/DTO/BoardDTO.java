package com.example.demo.DTO;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.BoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private int board_id;
	private int id;
	private String nickname;
	private List<MultipartFile> img;
	private String imgpath;
    private String video;
    private String content;
    private int like;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

	public static BoardDTO toDTO(BoardEntity entity) {
        return BoardDTO.builder()
                .board_id(entity.getBoard_id())
                .id(entity.getId())
                .nickname(entity.getNickname())
                .imgpath(entity.getImg())
                .video(entity.getVideo())
                .content(entity.getContent())
                .date(entity.getDate())
                .build();
    }


	public static List<BoardDTO> ToDtoList(List<BoardEntity> entities) {
	    List<BoardDTO> dtos = new ArrayList<>();
	    for (BoardEntity entity : entities) {
	        BoardDTO dto = new BoardDTO();
	        dto.setBoard_id(entity.getBoard_id());
	        dto.setId(entity.getId());
	        dto.setNickname(entity.getNickname());
	        dto.setImgpath(entity.getImg());
	        dto.setVideo(entity.getVideo());
	        dto.setDate(entity.getDate());
	        dto.setContent(entity.getContent());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}