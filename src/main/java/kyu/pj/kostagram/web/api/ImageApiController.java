package kyu.pj.kostagram.web.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.service.ImageService;
import kyu.pj.kostagram.service.LikesService;
import kyu.pj.kostagram.web.dto.CMRespDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {
	
	private final ImageService imageService;
	private final LikesService likesService;
	
	@GetMapping("/api/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails PrincipalDetails,
			@PageableDefault(size = 3) Pageable pageable){
		Page<Image> images = imageService.imageStory(PrincipalDetails.getUsers().getId(), pageable);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "성공", images), HttpStatus.OK);
	}
	
	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails PrincipalDetails){
		likesService.like(imageId, PrincipalDetails.getUsers().getId());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails PrincipalDetails){
		likesService.unLike(imageId, PrincipalDetails.getUsers().getId());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
	}
}
