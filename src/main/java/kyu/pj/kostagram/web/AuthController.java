package kyu.pj.kostagram.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.service.AuthService;
import kyu.pj.kostagram.util.Script;
import kyu.pj.kostagram.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller //1. IoC 2. 파일을 리턴하는 컨트롤러
public class AuthController {
	
	private final AuthService authService;
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		
		return "auth/signup";
	}
	
	@PostMapping("/auth/signup")
	@ResponseBody
	public String SignUp(@Valid SignupDto signupDto, BindingResult bindingResult) { //key = value (x-www-form-urlencoded)
		
		//Users <- SignDto
		Users users = signupDto.toEntity();
		Users usersEntity = authService.signUp(users);
		System.out.println(usersEntity);
		
		return Script.href("회원가입이 완료되었습니다", "/auth/signin");
	}
}
