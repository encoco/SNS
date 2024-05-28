package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.entity.CCMEntity;

public interface CCMRepository extends JpaRepository<CCMEntity, Integer>{

	List<CCMEntity> findBycommunitychatId(int communitychatId);
	
}
