package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.BoardLikeEntity;

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
public class BoardLikeDTO {
	private int boardlike_id;
	private int id;
	private int board_id;
	@Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	
	public static BoardLikeDTO toDTO(BoardLikeEntity entity) {
        return BoardLikeDTO.builder()
                .boardlike_id(entity.getBoardlike_id())
        		.board_id(entity.getBoard_id())
                .id(entity.getId())
                .date(entity.getDate())
                .build();
    }
	public static List<BoardLikeDTO> ToDtoList(List<BoardLikeEntity> entities) {
	    List<BoardLikeDTO> dtos = new ArrayList<>();
	    for (BoardLikeEntity entity : entities) {
	        BoardLikeDTO dto = new BoardLikeDTO();
	        dto.setBoard_id(entity.getBoard_id());
	        dto.setId(entity.getId());
	        dto.setDate(entity.getDate());
	        dtos.add(dto);
	    }
	    return dtos;
	}
}
