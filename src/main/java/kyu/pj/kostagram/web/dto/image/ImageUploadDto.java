package kyu.pj.kostagram.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.domain.users.Users;
import lombok.Data;

@Data
public class ImageUploadDto {
	
	private MultipartFile file;
	private String caption;
	
	public Image toEntity(String postImageUrl, Users users) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.users(users)
				.build();
	}
}
