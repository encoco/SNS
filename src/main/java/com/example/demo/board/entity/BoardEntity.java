package com.example.demo.board.entity;


import com.example.demo.board.dto.BoardDTO;
import com.example.demo.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Board")
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;

    private String img;
    private String video;
    private String content;
    private String date;

    public static BoardEntity toEntity(BoardDTO dto) {
        // UserEntity 객체를 별도로 생성
        UsersEntity userEntity = UsersEntity.builder()
                .id(dto.getId())
                .build();

        return BoardEntity.builder()
                .boardId(dto.getBoard_id())
                .user(userEntity)
                .img(dto.getImgpath())
                .video(dto.getVideo())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }

    public void setUpdateContent(String content2, String imgpath) {
        this.content = content2;
        this.img = imgpath;
    }
}