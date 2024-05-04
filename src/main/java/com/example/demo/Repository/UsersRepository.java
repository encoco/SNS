package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	public boolean existsById(int id);
	public boolean existsByUsername(String username);
	public boolean existsByNickname(String nickname);
	public UsersEntity findByUsername(String username);
	public List<UsersEntity> findBynicknameContaining(String searchTerm);
}


