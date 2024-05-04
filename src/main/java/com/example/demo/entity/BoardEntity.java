package com.example.demo.entity;



import com.example.demo.DTO.BoardDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Board")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int board_id; //DB가서 pk id로 바꾸기
	private int id;
	private String nickname;
	private String img;
    private String video;
    private String content;
    private String date;

    public static BoardEntity toEntity(BoardDTO dto) {
        return BoardEntity.builder()
        		.board_id(dto.getBoard_id())
                .id(dto.getId())
                .nickname(dto.getNickname())
                .img(dto.getImgpath())
                .video(dto.getVideo())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }

	@Override
	public String toString() {
		return "BoardEntity [board_id=" + board_id + ", id=" + id + ",nickname=" +nickname+ ", img=" + img  + ", video="
				+ video + ", content=" + content + ", date=" + date + "]";
	}

	public void setUpdateContent(String content2, String imgpath) {
		this.content = content2;
		this.img = imgpath;
	}

}