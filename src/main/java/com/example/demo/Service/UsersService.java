package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.Users;

@Service
public class UsersService {
	 private final UsersRepository usersRepository;
	 private final PasswordEncoder passwordEncoder;
	    
	 	@Autowired
	    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
	        this.usersRepository = usersRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public boolean isUserIdDuplicate(String userId) {
	        return usersRepository.existsById(userId);
	    }
	    
	    
	    public Users registerUser(Users user) {
	        // 비밀번호 암호화
	        String encodedPassword = passwordEncoder.encode(user.getPassword());
	        user.setPassword(encodedPassword);
	        // 암호화된 비밀번호와 함께 사용자 정보 저장
	        return usersRepository.save(user);
	    }

	    public Users findbyId(Users user) {
	        Users foundUser = usersRepository.findById(user.getId());
	        if (foundUser != null) {
	            String encodedPassword = foundUser.getPassword(); // 데이터베이스에서 가져온 인코딩된 비밀번호
	            if (passwordEncoder.matches(user.getPassword(), encodedPassword)) {
	                return foundUser; //성공
	            }
	            else return null; //비번 틀림
	        }
	        else return null;//계정 없음
	    }
}
