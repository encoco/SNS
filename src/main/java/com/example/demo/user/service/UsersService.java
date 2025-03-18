package com.example.demo.user.service;

import com.example.demo.user.dto.SearchDTO;
import com.example.demo.user.dto.UsersDTO;
import com.example.demo.user.dto.UsersInfoDTO;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UsersDTO user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UsersEntity entity = UsersEntity.toEntity(user);
        // 암호화된 비밀번호와 함께 사용자 정보 저장
        UsersEntity savedEntity = usersRepository.save(entity);
    }

    public List<SearchDTO> searchUsers(String searchTerm) {
        List<UsersEntity> entity = usersRepository.findBynicknameContaining(searchTerm);
        List<SearchDTO> dto = SearchDTO.toSearchDTO(entity);

        return dto;
    }

    public SearchDTO userInfo(int userId) {
        UsersEntity entity = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        SearchDTO dto = SearchDTO.toDTO(entity);

        return dto;
    }

    @Transactional
    public UsersInfoDTO updateUserProfile(UsersDTO profile) {
        try {
            UsersEntity entity = usersRepository.findByNickname(profile.getOriginal());
            entity.setNickname(profile.getNickname());
            entity.setState_message(profile.getState_message());
            if (profile.getImgpath() != null) {
                //entity.setProfile_img(bservice.uploadFile(profile.getImgpath(), "userProfile"));
            }
            usersRepository.save(entity);
            UsersInfoDTO dto = UsersInfoDTO.toInfoDTO(entity);
            return dto;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Transactional
    public boolean updatePassword(UsersDTO dto, int userId) {
        Optional<UsersEntity> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> {
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getChangePassword()));
                usersRepository.save(user);
                return true;
            } else {
                return false;
            }
        }).orElseGet(() -> {
            System.out.println("사용자를 찾을 수 없음");
            return false;
        });
    }

    public void DeleteUser(int userIdFromToken) {
        usersRepository.deleteById(userIdFromToken);
    }

}

