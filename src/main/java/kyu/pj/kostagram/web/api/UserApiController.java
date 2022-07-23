package kyu.pj.kostagram.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.handler.ex.CustomValidationAPIException;
import kyu.pj.kostagram.service.SubscribeService;
import kyu.pj.kostagram.service.UsersService;
import kyu.pj.kostagram.web.dto.CMRespDto;
import kyu.pj.kostagram.web.dto.subscribe.SubscribeDto;
import kyu.pj.kostagram.web.dto.users.UsersUpdateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UsersService usersService;
	private final SubscribeService subscribeService;
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id,
			@Valid UsersUpdateDto usersUpdateDto,
			BindingResult bindingResult, //@Valid 다음 파라미터에 안넣으면 안먹힘
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Users usersEntity = usersService.profileUpdate(id, usersUpdateDto.toEntity());
		principalDetails.setUsers(usersEntity); //session 정보 변경
		
		return new CMRespDto<>(1, "정보 변경 완료", usersEntity); //응답시에 userEntity의 모든 getter 함수가 호출되고 json으로 파싱하여 응답
	}
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile,
			@AuthenticationPrincipal PrincipalDetails principalDetails){
		
		Users usersEntity = usersService.profileImageUpdate(principalId, profileImageFile);
		principalDetails.setUsers(usersEntity);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진이 변경되었습니다", null), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		List<SubscribeDto> subscribeDto = subscribeService.subscribeList(principalDetails.getUsers().getId(), pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}
}
