package com.example.demo.follow.repository;

import com.example.demo.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {
    public boolean existsByFollowerIdAndFollowingId(int followerId, int followingId);

    public FollowEntity findByFollowerIdAndFollowingId(int followerId, int followingId);

    @Query(value = "SELECT following_id FROM follow WHERE follower_id = :followerId", nativeQuery = true)
    List<Integer> findFollowingIdByFollowerId(@Param("followerId") int followerId);
}
