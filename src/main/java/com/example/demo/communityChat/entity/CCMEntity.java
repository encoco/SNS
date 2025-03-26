package com.example.demo.communityChat.entity;

import com.example.demo.communityChat.dto.CCMDTO;
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
@Table(name = "communitychatmessage")
@AllArgsConstructor
@NoArgsConstructor
public class CCMEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commessage_id")
    int commessageId;
    @Column(name = "communitychat_id")
    int communitychatId;

    int id;
    String nickname;
    String profile_img;


    String content;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    //Entity만들고 컨트롤러에 그룹챗 넘어오는지 확인부터 다시 시작

    public static CCMEntity toEntity(CCMDTO dto) {
        return CCMEntity.builder()
                .commessageId(dto.getCommessage_id())
                .communitychatId(dto.getCommunitychat_id())
                .id(dto.getId())
                .nickname(dto.getNickname())
                .content(dto.getContent())
                .date(dto.getDate())
                .build();
    }
}
