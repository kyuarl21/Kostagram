package kyu.pj.kostagram.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.handler.exception.CustomValidationException;
import kyu.pj.kostagram.service.ImageService;
import kyu.pj.kostagram.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;
	
	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular(Model model) {
		List<Image> images = imageService.popularImage();
		model.addAttribute("images", images);
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(imageUploadDTO.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다", null);
		}
		//서비스 호출
		imageService.imageUpload(imageUploadDTO, principalDetails);
		
		return "redirect:/user/" + principalDetails.getUser().getId();
	}
}
