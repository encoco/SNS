package com.example.demo.follow.dto;

import com.example.demo.follow.entity.FollowEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "팔로우 시 상대방과 본인의 ID를 담는 DTO")
public class FollowDTO {
    @Schema(description = "각 팔로우 기록들의 PKey", example = "1")
    private int follow_id;

    @Schema(description = "내 ID", example = "12")
    private int followerId;

    @Schema(description = "상대방 ID", example = "132")
    private int followingId;

    public static FollowDTO toDTO(FollowEntity entity) {
        return FollowDTO.builder()
                .followerId(entity.getFollowerId())
                .followingId(entity.getFollowingId())
                .build();
    }
}
