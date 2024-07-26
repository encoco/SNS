package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.DTO.CommentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@Entity
public class CommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//
	private int comment_id;
	@Column(name = "board_id")
	private int boardId;
	private int id;
	private String comment;
	private String nickname;
	private String profile_img;
	
	@Builder.Default
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

	public static CommentEntity toEntity(CommentDTO dto) {
		return CommentEntity.builder()
				.comment_id(dto.getComment_id())
				.boardId(dto.getBoard_id())
				.id(dto.getId())
				.nickname(dto.getNickname())
				.comment(dto.getComment())
				.date(dto.getDate())
				.build();
	}

	public static List<CommentDTO> ToDtoList(List<CommentEntity> entities) {
	    List<CommentDTO> dtos = new ArrayList<>();
	    for (CommentEntity entity : entities) {
	    	CommentDTO dto = new CommentDTO();
	    	dto.setComment_id(entity.getComment_id());
	        dto.setBoard_id(entity.getBoardId());
	        dto.setId(entity.getId());
	        dto.setComment(entity.getComment());
	        dto.setProfile_img(entity.getProfile_img());
	        dto.setNickname(entity.getNickname());
	        dto.setDate(entity.getDate());

	        dtos.add(dto);
	    }
	    return dtos;
	}
}

