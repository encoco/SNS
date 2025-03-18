package com.example.demo.follow.entity;

import com.example.demo.follow.dto.FollowDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "Follow")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int follow_id;

    @Column(name = "follower_id")
    private int followerId;

    @Column(name = "following_id")
    private int followingId;

    public static FollowEntity toEntity(FollowDTO dto) {
        return FollowEntity.builder()
                .followerId(dto.getFollowerId())
                .followingId(dto.getFollowingId())
                .build();
    }

}
