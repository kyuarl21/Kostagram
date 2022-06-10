package kyu.pj.kostagram.web.dto.users;

import javax.validation.constraints.NotBlank;

import kyu.pj.kostagram.domain.users.Users;
import lombok.Data;

@Data
public class UsersUpdateDto {
	@NotBlank
	private String name; //필수
	@NotBlank
	private String password; //필수
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	public Users toEntity() {
		
		return Users.builder()
				.name(name)
				.password(password)
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
