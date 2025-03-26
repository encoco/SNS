package com.example.demo;

import com.example.demo.board.entity.BoardEntity;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.user.dto.UsersDTO;
import com.example.demo.user.entity.UsersEntity;
import com.example.demo.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements CommandLineRunner{

    private final UsersRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker(new Locale("ko")); // 한글 faker

    @Override
    public void run(String... args) {
        if (userRepository.count() == 3) {
            System.out.println("user 저장 시작");
            createDummyUsers(100);
            System.out.println("user 저장 완료");
        }

        if (boardRepository.count() == 2) {
            System.out.println("board 저장 시작");
            createDummyPosts(200);
            System.out.println("board 저장 완료");
        }
    }

    private void createDummyUsers(int count) {
        List<UsersEntity> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UsersDTO dto = new UsersDTO();
            dto.setEmail(faker.internet().emailAddress());
            dto.setPassword(passwordEncoder.encode("1234")); // 🔐 비번 1234 → 인코딩
            dto.setNickname(faker.name().fullName());
            dto.setUsername(faker.name().fullName());

            UsersEntity user = UsersEntity.toEntity(dto);
            users.add(user);
        }

        userRepository.saveAll(users);
    }

    private void createDummyPosts(int count) {
        List<UsersEntity> users = userRepository.findAll();
        Random random = new Random();

        List<BoardEntity> posts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UsersEntity author = users.get(random.nextInt(users.size()));

            BoardEntity post = BoardEntity.builder()
                    .user(author)
                    .content(faker.lorem().paragraph())
                    .img(null)
                    .video(null)
                    .date(null)
                    .build();

            posts.add(post);
        }

        boardRepository.saveAll(posts);
    }
}