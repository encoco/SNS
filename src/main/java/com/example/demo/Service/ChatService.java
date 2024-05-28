package com.example.demo.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.CCJDTO;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.DTO.ChatMessageDTO;
import com.example.demo.DTO.CommunityChatDTO;
import com.example.demo.Repository.ChatMessageRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Repository.CommuChatJoinRepository;
import com.example.demo.Repository.CommuChatRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.entity.ChatEntity;
import com.example.demo.entity.ChatMessageEntity;
import com.example.demo.entity.CommunityChatEntity;
import com.example.demo.entity.CommunityChatJoinEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final ChatMessageRepository messageRepository;
	private final UsersRepository uRepository;
	private final CommuChatRepository commuRepository;
	private final CommuChatJoinRepository CCJRepository;
	private final BoardService boardService; 
	
	public List<ChatDTO> selectRoom(int userId) {
		List<ChatEntity> entity = chatRepository.findByUserId(userId);
		if (entity != null) {
			List<ChatDTO> dto = ChatDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}

	@Transactional
	public ChatDTO CreateRoom(List<Integer> userIds, int myId) {
		String roomNumber = UUID.randomUUID().toString();
		String name = "";
		String ids = makeIds(userIds);

		while (chatRepository.existsByRoomNumber(roomNumber)) {
			roomNumber = UUID.randomUUID().toString();
		}

		for (Integer userId : userIds) {
			ChatEntity chatUser = new ChatEntity();
			name = "";
			for (Integer id : userIds) {
				if (userId == id)
					continue;
				if (name.length() > 0)
					name += ",";
				name += uRepository.findNicknameById(id);
			}
			chatUser.setRoomname(name);
			chatUser.setRoomNumber(roomNumber);
			chatUser.setUserId(userId);
			chatUser.setJoinId(ids);
			chatRepository.save(chatUser);
		}
		ChatEntity create = chatRepository.findByJoinIdAndUserId(ids, myId);
		return ChatDTO.toDTO(create);
	}

	public ChatDTO findRoom(List<Integer> userIds, int myId) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		ChatEntity selectE = chatRepository.findByJoinIdAndUserId(userIdsString, myId);
		if (selectE != null)
			return ChatDTO.toDTO(selectE);
		return null;
	}

	public String makeIds(List<Integer> userIds) {
		Collections.sort(userIds);
		String userIdsString = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		return userIdsString;
	}

	public void saveChat(ChatMessageDTO message) {
		ChatMessageEntity entity = ChatMessageEntity.toEntity(message);
		messageRepository.save(entity);
	}

	public List<ChatMessageDTO> getMessage(String roomNumber) {
		List<ChatMessageEntity> entity = messageRepository.findByroomNumber(roomNumber);
		return ChatMessageDTO.ToDtoList(entity);
	}

	public void CreateCommChat(CommunityChatDTO dto) {
		CommunityChatEntity entity = CommunityChatEntity.toEntity(dto);
		if(dto.getImg() != null) entity.setImg(boardService.uploadFile(dto.getImg(), "CommunityChat"));
		commuRepository.save(entity);
		
	}

	public List<CommunityChatDTO> selectAllCommuRoom() {
		List<CommunityChatEntity> entity = commuRepository.findAll();
		return CommunityChatDTO.toDTOList(entity);
		
	}

	public void joinCommunity(CommunityChatDTO dto) {
		CommunityChatJoinEntity entity = CommunityChatJoinEntity.toEntity(dto);
		CCJRepository.save(entity);
	}

	public List<CCJDTO> selectCommuRoom(int userIdFromToken) {
		return CCJDTO.toDTOList(CCJRepository.findAllById(userIdFromToken));
	}
}
