package kyu.pj.kostagram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kyu.pj.kostagram.domain.subscribe.SubscribeRepository;
import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.domain.users.UsersRepository;
import kyu.pj.kostagram.handler.ex.CustomApiException;
import kyu.pj.kostagram.handler.ex.CustomException;
import kyu.pj.kostagram.handler.ex.CustomValidationAPIException;
import kyu.pj.kostagram.web.dto.users.UsersProfileDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsersService {
	
	private final UsersRepository usersRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional(readOnly = true) //readOnly -> Dirty Checking을 안함
	public UsersProfileDto userProfile(int pageUserId, int principalId) {
		UsersProfileDto dto = new UsersProfileDto();
		
		//SELECT * FROM image WHERE userId = :userId;
		Users usersEntity = usersRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다");
		});
		
		dto.setUsers(usersEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(usersEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mySubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mySubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		//프로필 내에서 좋아요 카운트 만들기
		usersEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public Users profileUpdate(int id, Users users) {
		//1.영속화
		//무조건 찾았음: get() -> 못 찾았음: Exception 발동 orElseThrow()
		Users usersEntity = usersRepository.findById(id).orElseThrow(() -> {

			return new CustomValidationAPIException("존재하지 않는 사용자입니다");
		});
		//2.영속화된 Object를 수정 - Dirty Checking(업데이트 완료)
		usersEntity.setName(users.getName());
		String rawPassword = users.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		usersEntity.setPassword(encPassword);
		usersEntity.setWebsite(users.getWebsite());
		usersEntity.setBio(users.getBio());
		usersEntity.setPhone(users.getPhone());
		usersEntity.setGender(users.getGender());
		
		return usersEntity;
	}
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public Users profileImageUpdate(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		System.out.println("FileName: " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Users usersEntity = usersRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("사용자를 찾을 수 없습니다");
		});
		usersEntity.setProfileImageUrl(imageFileName);
		
		return usersEntity;
	} //Dirty Checking으로 업데이트됨
}
