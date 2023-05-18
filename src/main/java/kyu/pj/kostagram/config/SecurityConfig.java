package kyu.pj.kostagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kyu.pj.kostagram.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity //해당 파일로 Security를 활성화
@Configuration //IoC
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//기존 Security기능을 전부 비활성화
		
		http.csrf().disable(); //csrf토큰 비활성화
		http.authorizeRequests() //인증이 되지않은 모든 사용자는 로그인 페이지로
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin") //Get
			.loginProcessingUrl("/auth/signin") //Post -> Spring Security가 로그인 프로세스 진행
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login() //form login, oauth2 login 둘다
			.userInfoEndpoint() //oauth2 login 하면 응답을 회원정보로 바로 받아짐
			.userService(oAuth2DetailsService);			
	}
}
