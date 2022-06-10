package kyu.pj.kostagram.web.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import kyu.pj.kostagram.domain.users.Users;
import lombok.Data;

@Data
public class SignupDto {
	//https://bamdule.tistory.com/35 (@Valid 어노테이션 종류)
	@Size(min=2, max=20)
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	
	public Users toEntity() {
		
		return Users.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}
