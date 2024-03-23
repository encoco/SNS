package com.example.demo.Config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.UsersEntity;

import DTO.UsersDTO;
import lombok.RequiredArgsConstructor;


//시큐리티 설정에서 loginProcessingUrl("/");
// "/" 요청이 오면 자동으로 UserDetailsService타입으로 IOC되어있는 loadUserbyUserName 함수가 실행.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UsersRepository userRepository;

	//시큐리티 session(내부 Authentication(내부 UserDetails))


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    UsersEntity userEntity = userRepository.findByUsername(username);
	    if(userEntity == null) {
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
	    UsersDTO userDTO = UsersDTO.toDTO(userEntity);
	    return new PrincipalDetails(userDTO);
	}
}
