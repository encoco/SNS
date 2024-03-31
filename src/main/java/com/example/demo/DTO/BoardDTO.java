package com.example.demo.DTO;


import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.BoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private int board_id;
	private int id;
	private String img;
    private String video;
    private String content;
	private DateTime date;


	public static BoardDTO toDTO(BoardEntity dto) {
        return BoardDTO.builder()
                .board_id(dto.getBoard_id())
                .id(dto.getId())
                .img(dto.getImg())
                .video(dto.getVideo())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }



	@Override
	public String toString() {
		return "BoardDTO [board_id=" + board_id +" id=" + id + ", img=" + img + ", video=" + video + ", content=" + content
				+ ", date=" + date+ "]";
	}



}