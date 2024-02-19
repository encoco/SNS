package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsersRepository;

@Service
public class UsersService {
	 private final UsersRepository usersRepository;

	    @Autowired
	    public UsersService(UsersRepository usersRepository) {
	        this.usersRepository = usersRepository;
	    }

	    public boolean isUserIdDuplicate(String userId) {
	        return usersRepository.existsById(userId);
	    }
}
