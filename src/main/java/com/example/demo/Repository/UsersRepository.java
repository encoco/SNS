package com.example.demo.Repository; // 패키지 명이 일반적으로 소문자로 시작합니다.


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UsersEntity; // Users 엔티티의 패키지 경로를 정확하게 지정해야 합니다.

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	public boolean existsById(int id);
	public boolean existsByUsername(String username);
	public UsersEntity findByUsername(String username);
}


