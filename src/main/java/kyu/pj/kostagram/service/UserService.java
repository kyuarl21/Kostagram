package kyu.pj.kostagram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import kyu.pj.kostagram.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kyu.pj.kostagram.domain.subscribe.SubscribeRepository;
import kyu.pj.kostagram.domain.user.UserRepository;
import kyu.pj.kostagram.handler.exception.CustomApiException;
import kyu.pj.kostagram.handler.exception.CustomException;
import kyu.pj.kostagram.handler.exception.CustomValidationAPIException;
import kyu.pj.kostagram.web.dto.user.UserProfileDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional(readOnly = true) //readOnly -> Dirty Checking을 안함
	public UserProfileDto userProfile(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		//SELECT * FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mySubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mySubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		//프로필 내에서 좋아요 카운트 만들기
		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public User profileUpdate(int id, User user) {
		//1.영속화
		//무조건 찾았음: get() -> 못 찾았음: Exception 발동 orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
			return new CustomValidationAPIException("존재하지 않는 사용자입니다");
		});
		//2.영속화된 Object를 수정 - Dirty Checking(업데이트 완료)
		userEntity.setName(user.getName());
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		userEntity.setPassword(encPassword);
		userEntity.setWebsite(user.getWebsite());
		userEntity.setBio(user.getBio());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public User profileImageUpdate(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		System.out.println("FileName: " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("사용자를 찾을 수 없습니다");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	} //Dirty Checking으로 업데이트됨
}
