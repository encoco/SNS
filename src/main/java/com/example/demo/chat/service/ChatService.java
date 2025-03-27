package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatDTO;
import com.example.demo.chat.dto.ChatMessageDTO;
import com.example.demo.chat.entity.ChatRoomEntity;
import com.example.demo.chat.entity.ChatMessageEntity;
import com.example.demo.chat.entity.ChatMemberEntity;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.ChatMemberRepository;
import com.example.demo.chat.repository.ChatRepository;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatParticipantRepository;
    private final ChatMessageRepository messageRepository;
    private final UsersRepository usersRepository;

    /**
     * 내가 참여한 채팅방 목록 조회
     */
    public List<ChatDTO> selectRoom(int userId) {
        List<ChatMemberEntity> participants = chatParticipantRepository.findByUser_Id(userId);

        return participants.stream()
                .map(ChatMemberEntity::getChatRoom)
                .distinct()
                .map(chat -> ChatDTO.toDTO(chat, userId))  // ✅ 내 ID 넘겨줌
                .collect(Collectors.toList());
    }

    /**
     * 채팅방 생성
     */
    @Transactional
    public ChatDTO createRoom(List<Integer> userIds, int myId) {
        System.out.println("없으니까 만들게~ createRoom");
        UsersEntity creator = usersRepository.findById(myId).orElseThrow();
        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                .user(creator)
                .build();
        chatRepository.save(chatRoom);

        // 참여자 등록
        for (Integer userId : userIds) {
            UsersEntity user = usersRepository.findById(userId).orElseThrow();
            ChatMemberEntity participant = ChatMemberEntity.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .date(chatRoom.getDate())
                    .build();
            chatParticipantRepository.save(participant);
        }

        // 본인도 추가
        if (!userIds.contains(myId)) {
            ChatMemberEntity selfParticipant = ChatMemberEntity.builder()
                    .chatRoom(chatRoom)
                    .user(creator)
                    .date(chatRoom.getDate())
                    .build();
            chatParticipantRepository.save(selfParticipant);
        }
        ChatDTO dto = ChatDTO.toDTO(chatRoom,myId);
        dto.setNew_room(true);
        return dto;
    }

    /**
     * 주어진 유저 ID들과 정확히 일치하는 채팅방을 찾는다
     */
    public ChatDTO findRoom(List<Integer> userIds, int myId) {
        System.out.println("아니왜요 갑자기?" + userIds + "    " + myId);
        List<ChatMemberEntity> myChats = chatParticipantRepository.findByUser_Id(myId);
        System.out.println("findRoom myChats 결과 : " + myChats);
        for (ChatMemberEntity participant : myChats) {
            ChatRoomEntity chatRoom = participant.getChatRoom();
            List<Integer> participantIds = chatParticipantRepository.findByChatRoom_Id(chatRoom.getId())
                    .stream()
                    .map(p -> p.getUser().getId())
                    .sorted()
                    .toList();

            System.out.println("참여자 Id : " + participantIds);
            List<Integer> input = new ArrayList<>(userIds);
            input.sort(Comparator.naturalOrder());
            System.out.println("input : " + input);

            if (participantIds.equals(userIds)) {
                System.out.println("있으니까 넘길게~ participantIds.equals(input)");
                ChatDTO dto = ChatDTO.toDTO(chatRoom,myId);
                dto.setNew_room(false);
                return dto;
            }
        }

        return null;
    }

    /**
     * 1:1 채팅방을 찾거나 없으면 새로 만든다
     */
    @Transactional
    public ChatDTO findOrCreateRoom(List<Integer> userIds, int myId) {
        System.out.println("???");
        ChatDTO existing = findRoom(userIds, myId);
        // 있으면 false  없으면 True   방 생성 하냐/ 안하냐 기준
        return (existing != null) ? existing : createRoom(userIds, myId);
    }

    /**
     * 채팅 메시지 저장
     */
    public ChatMessageDTO saveChat(ChatMessageDTO message) {
        message.setNickname(usersRepository.findNicknameById(message.getId()));
        ChatMessageEntity entity = ChatMessageEntity.toEntity(message);
        messageRepository.save(entity);

        UsersEntity user = usersRepository.findById(entity.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        message.setProfile_img(user.getProfile_img());
        return message;
    }

    /**
     * 채팅방 내 메시지 조회
     */
    public List<ChatMessageDTO> getMessage(int userchatId) {
        List<ChatMessageEntity> entity = messageRepository.findByChatRoom_Id(userchatId);
        return ChatMessageDTO.ToDtoList(entity);
    }

}
