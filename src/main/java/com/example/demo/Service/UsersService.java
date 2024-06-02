package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.AlarmDTO;
import com.example.demo.DTO.SearchDTO;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.DTO.UsersInfoDTO;
import com.example.demo.DTO.followDTO;
import com.example.demo.Repository.AlarmRepository;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Repository.followRepository;
import com.example.demo.entity.AlarmEntity;
import com.example.demo.entity.UsersEntity;
import com.example.demo.entity.followEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
	private final UsersRepository usersRepository;
	private final followRepository fRepository;
	private final AlarmRepository alarmRepository;
	private final PasswordEncoder passwordEncoder;
	private final BoardService bservice;

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
				return foundUser.getUsername(); // 성공
			} else {
				return null; // 비번 틀림
			}
		} else {
			return null;// 계정 없음
		}
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
	public String followUser(int userId, int myId) {
		followEntity entity = fRepository.findByFollowerIdAndFollowingId(myId, userId);
		if (entity != null) {
			fRepository.delete(entity);
			return "del";
		} else {
			followDTO dto = new followDTO();
			dto.setFollowerId(myId);
			dto.setFollowingId(userId);
			entity = followEntity.toEntity(dto);
			Optional<AlarmEntity> existingAlarm = alarmRepository.findByCriteria(userId, myId,"님이 팔로우하셨습니다.",0);
			if (!existingAlarm.isPresent()  && userId != myId ) {
	            AlarmEntity alarmEntity = new AlarmEntity();
	            UsersEntity recipient = new UsersEntity();
	            recipient.setId(myId);
	            alarmEntity.setRecipientId(userId);
	            alarmEntity.setSender(recipient);
	            alarmEntity.setContent("님이 팔로우하셨습니다.");

	            alarmRepository.save(alarmEntity);
			}
			fRepository.save(entity);
			return "add";
		}
	}

	@Transactional
	public UsersInfoDTO updateUserProfile(UsersDTO profile) {
		try {
			UsersEntity entity = usersRepository.findByNickname(profile.getOriginal());
			entity.setNickname(profile.getNickname());
			entity.setState_message(profile.getState_message());
			if (profile.getImgpath() != null) {
				entity.setProfile_img(bservice.uploadFile(profile.getImgpath(), "userProfile"));
			}
			usersRepository.save(entity);
			UsersInfoDTO dto = UsersInfoDTO.toInfoDTO(entity);
			return dto;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public List<AlarmDTO> getAlarm(int id) {
		List<AlarmEntity> entity = alarmRepository.findByRecipientIdOrderByDateDesc(id);
		return AlarmDTO.toDtoList(entity);
	}

	public void delAllAlarm(int id) {
		List<AlarmEntity> entity = alarmRepository.findByRecipientId(id);
		alarmRepository.deleteAll(entity);
	}
}

