package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.UsersDTO;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.UsersEntity;

@Service
public class UsersService {
	 private final UsersRepository usersRepository;
	 private final PasswordEncoder passwordEncoder;

 	@Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserIdDuplicate(String username) {
        return usersRepository.existsByUsername(username);
    }


    public String registerUser(UsersDTO user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UsersEntity entity = UsersEntity.toEntity(user);
        // 암호화된 비밀번호와 함께 사용자 정보 저장
        UsersEntity savedEntity = usersRepository.save(entity);
        // 저장된 엔티티의 ID 반환
        return String.valueOf(savedEntity.getUsername()); // getUserId()가 사용자 ID를 반환한다고 가정
    }

    public String findbyId(UsersDTO user) {
        UsersEntity foundUser = usersRepository.findByUsername(user.getUsername());

        if (foundUser != null) {
            String encodedPassword = foundUser.getPassword(); // 데이터베이스에서 가져온 인코딩된 비밀번호
            if (passwordEncoder.matches(user.getPassword(), encodedPassword)) {
                return foundUser.getUsername(); //성공
            }
			else {
				return null; //비번 틀림
			}
        }
		else {
			return null;//계정 없음
		}
    }
}
