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


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private int board_id;
	private int id;
	private MultipartFile img;
	private String imgpath;
    private String video;
    private String content;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));


	@Override
	public String toString() {
	    return "BoardDTO{" +
	            "board_id=" + board_id +
	            ", id=" + id +
	            ", img='" + img + '\'' +
	            ", video='" + video + '\'' +
	            ", content='" + content + '\'' +
	            ", date=" + date +
	            '}';
	}

	public static BoardDTO toDTO(BoardEntity dto) {
        return BoardDTO.builder()
                .board_id(dto.getBoard_id())
                .id(dto.getId())
                .imgpath(dto.getImg())
                .video(dto.getVideo())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }


	public static List<BoardDTO> ToDtoList(List<BoardEntity> entities) {
	    List<BoardDTO> dtos = new ArrayList<>();
	    for (BoardEntity entity : entities) {
	        BoardDTO dto = new BoardDTO();
	        dto.setBoard_id(entity.getBoard_id());
	        dto.setId(entity.getId());
	        dto.setImgpath(entity.getImg());
	        dto.setVideo(entity.getVideo());
	        dto.setDate(entity.getDate());
	        dto.setContent(entity.getContent());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}