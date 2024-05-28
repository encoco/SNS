package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	public boolean existsById(int id);
	public boolean existsByUsername(String username);
	public boolean existsByNickname(String nickname);

	public UsersEntity findByUsername(String username);
	public UsersEntity findByNickname(String nickname);
	public List<UsersEntity> findBynicknameContaining(String searchTerm);

	@Query(value = "SELECT nickname FROM users WHERE id = :userId", nativeQuery = true)
    public String findNicknameById(@Param("userId")Integer userId);
}


