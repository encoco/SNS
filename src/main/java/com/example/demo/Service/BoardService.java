package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.Config.S3Config;
import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.BoardLikeDTO;
import com.example.demo.Repository.BoardLikeRepository;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.BoardLikeEntity;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;
	public final BoardLikeRepository boardlike;
	private final S3Config s3Config;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension; //고유번호 + 확장자로 이름 리네임

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize()); // 파일 크기 설정

        // 	S3에 파일 업로드
        try {
			s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket + "/" + path, newFilename, file.getInputStream(), metadata));
		} catch (SdkClientException | java.io.IOException e) {
			e.printStackTrace();
		}
        
        // 올바른 경로의 URL 주소를 생성하여 저장
        String s3Url = s3Config.amazonS3Client().getUrl(bucket + "/"+path, newFilename).toString();
        return s3Url; //db에 url 저장을 위해 return
    }
    
    
	public List<BoardDTO> getPost(int id){
		if(boardRepository.findByid(id) != null) {
			List<BoardEntity> entity = boardRepository.findByid(id);
			List<BoardDTO> dto = BoardDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}
	public List<BoardLikeDTO> getLike(int id) {
		if(boardRepository.findByid(id) != null) {
			List<BoardLikeEntity> entity = boardlike.findByUserId(id);
			List<BoardLikeDTO> dto = BoardLikeDTO.ToDtoList(entity);
			return dto;
		}
		return null;
	}
	public void writeBoard(BoardDTO boardDTO) {
		String imgPath = "";
		
		if(boardDTO.getImg() != null) {
			for(MultipartFile img : boardDTO.getImg()) {
				imgPath += "|" + uploadFile(img, "image");
			}
			
		}
		boardDTO.setImgpath(imgPath);
		BoardEntity board = BoardEntity.toEntity(boardDTO);
		boardRepository.save(board);
	}

	public int boardLike(BoardLikeDTO dto) {
		BoardLikeEntity entity = BoardLikeEntity.toEntity(dto);
		Optional<BoardLikeEntity> existingLike = boardlike.findByBoardIdAndUserId(dto.getBoard_id(), dto.getId());
		
		if (existingLike.isPresent()) {
	        // 이미 "좋아요"가 존재한다면 삭제
			boardlike.delete(existingLike.get());
	        System.out.println("Like removed for board_id=" + dto.getBoard_id() + ", user_id=" + dto.getId());
	        return 0;
	    } else {
	        // "좋아요"가 없다면 새로 저장
	    	boardlike.save(entity);
	        System.out.println("Like added for board_id=" + dto.getBoard_id() + ", user_id=" + dto.getId());
	        return 1;
	    }
	}

	@Transactional // 트랜잭션을 사용하여 업데이트를 보장
    public void updatePost(BoardDTO dto) { //findById == select * from board where board_id = dto.getBoard_id(); 
        BoardEntity post = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        String imgpath = dto.getImgpath();
        if( dto.getImg() != null) {
			for(MultipartFile img : dto.getImg()) {
				if(imgpath.isEmpty()|| imgpath=="") {
					imgpath += uploadFile(img, "image");
				}
				else {
					imgpath += "|" + uploadFile(img, "image");
				}
			}
		}
        post.setUpdateContent(dto.getContent(),imgpath);
    }

	@Transactional
	public void DeleteBoard(int board_id) {
		boardRepository.deleteById(board_id);
	}
}
