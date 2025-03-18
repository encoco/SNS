package com.example.demo.communityChat.entity;

import com.example.demo.communityChat.dto.CommunityChatDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@Entity
@Table(name = "communitychat_join")
@AllArgsConstructor
@NoArgsConstructor
public class CommunityChatJoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccj_id")
    private int ccjId;
    @Column(name = "communitychat_id")
    private int communitychatId;
    private String title;
    private String imgpath;
    private int id;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

    public static CommunityChatJoinEntity toEntity(CommunityChatDTO entity) {
        return CommunityChatJoinEntity.builder()
                .communitychatId(entity.getCommunitychatId())
                .title(entity.getTitle())
                .imgpath(entity.getImgpath())
                .id(entity.getId())
                .date(entity.getDate())
                .build();
    }


}
