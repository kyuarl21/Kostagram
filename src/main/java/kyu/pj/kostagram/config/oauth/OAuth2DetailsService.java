package kyu.pj.kostagram.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {
	
	private final UsersRepository usersRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> userInfo = oAuth2User.getAttributes();
		String username = "facebook_" + (String)userInfo.get("id");
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
		String email = (String)userInfo.get("email");
		String name = (String)userInfo.get("name");
		
		Users usersEntity = usersRepository.findByUsername(username);
		if(usersEntity == null) { //최초 페이스북 로그인
			Users users = Users.builder()
					.username(username)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();
			
			return new PrincipalDetails(usersRepository.save(users), oAuth2User.getAttributes());
		}else { //이미 페이스북으로 가입된 회원임
			return new PrincipalDetails(usersEntity, oAuth2User.getAttributes());
		}
	}
}
