package kyu.pj.kostagram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.domain.image.ImageRepository;
import kyu.pj.kostagram.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public void imageUpload(ImageUploadDto imageUploadDTO, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + imageUploadDTO.getFile().getOriginalFilename();
		//System.out.println("FileName: " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFilePath, imageUploadDTO.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//image 테이블에 저장
		Image image = imageUploadDTO.toEntity(imageFileName, principalDetails.getUser());
		imageRepository.save(image);
		
		//System.out.println(imageEntity); //Object가 실행되면 toString()이 자동 호출
	}
	
	@Transactional(readOnly = true) //읽기 전용 (영속성 컨텍스트 변경 감지를 안함 즉 Dirty Checking을 안함)
	public Page<Image> imageStory(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.myStory(principalId, pageable);
		
		//images에 좋아요 상태 담기, images를 뽑아서 좋아요가 눌린 사진들을 뽑아내고 현재 로그인 되있는 유저가 누른 좋아요인지 비교
		images.forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
			image.getLikes().forEach((like) -> {
				if(like.getUser().getId() == principalId) {
					image.setLikeState(true);
				}
			});
		});
		return images;
	}
	
	@Transactional(readOnly = true)
	public List<Image> popularImage(){
		return imageRepository.myPopular();
	}
}
