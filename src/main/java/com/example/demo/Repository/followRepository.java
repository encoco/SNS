package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.entity.followEntity;

public interface followRepository  extends JpaRepository<followEntity, Integer>{
	public boolean existsByFollowerIdAndFollowingId(int followerId, int followingId);
	public followEntity findByFollowerIdAndFollowingId(int followerId, int followingId);
	
	@Query(value = "SELECT following_id FROM follow WHERE follower_id = :followerId", nativeQuery = true)
    List<Integer> findFollowingIdByFollowerId(@Param("followerId") int followerId);
}
