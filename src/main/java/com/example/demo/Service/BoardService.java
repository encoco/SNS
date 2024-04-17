package com.example.demo.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.Config.S3Config;
import com.example.demo.DTO.BoardDTO;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.entity.BoardEntity;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;
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

	public void writeBoard(BoardDTO boardDTO) {
		String imgPath = "";
		if(boardDTO.getImg() != null) {
			for(MultipartFile img : boardDTO.getImg()) {
				imgPath += uploadFile(img, "image") + "|";
			}
		}
		if(imgPath.length() > 0) {
			imgPath = imgPath.substring(0,imgPath.length()-1);
		}
		boardDTO.setImgpath(imgPath);
		BoardEntity board = BoardEntity.toEntity(boardDTO);
		boardRepository.save(board);
	}

}
