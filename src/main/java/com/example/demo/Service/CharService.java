package com.example.demo.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.ChatEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharService {
	private final ChatRepository chatRepository;
	private final UsersRepository uRepository;
	public List<ChatDTO> selectRoom(int userId) {
		List<ChatEntity> entity = chatRepository.findByUserId(userId);
		System.out.println("entity : " + entity);
		if (entity != null) {
			List<ChatDTO> dto = ChatDTO.ToDtoList(entity);
			System.out.println(dto);
			return dto;
		}
		return null;
	}

	@Transactional
	public ChatDTO CreateRoom(List<Integer> userIds,int myId) {
		String roomNumber = UUID.randomUUID().toString();
		String name = "";
		ChatDTO dto = new ChatDTO();
		
		while (chatRepository.existsByRoomNumber(roomNumber)) {
            roomNumber = UUID.randomUUID().toString();
        }
		
		for (Integer userId : userIds) {
			if(userId == myId) continue;
			if (name.length() > 0) {
	            name += ",";
	        }
			name += uRepository.findNicknameById(userId);  
		}
		
		for (Integer userId : userIds) {
			ChatEntity chatUser = new ChatEntity();
			chatUser.setNickname(name);
			chatUser.setRoomNumber(roomNumber);
			chatUser.setUserId(userId);
			chatUser.setNickname(name);
			chatRepository.save(chatUser);
			if(userId == myId) {
				dto.setId(myId);
				dto.setRoomNumber(roomNumber);
				dto.setNickname(name);
			}
		}
		
		return dto;
	}
}
