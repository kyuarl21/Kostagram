package kyu.pj.kostagram.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.service.UsersService;
import kyu.pj.kostagram.web.dto.users.UsersProfileDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UsersService usersService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		UsersProfileDto dto = usersService.userProfile(pageUserId, principalDetails.getUsers().getId());
		model.addAttribute("dto", dto);
		
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		//AuthenticationPrincipal 어노테이션으로 꺼내는 방법
		//System.out.println("@session: " + principalDetails.getUsers());
		
		//직접 꺼내는 방법 (sessoin안에 SecurityContextHolder안에 Authentication안에 UserDetails안에 UserObject가 있음)
		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails principalDetails1 = (PrincipalDetails)auth.getPrincipal();
		System.out.println("session: " + principalDetails1.getUsers());*/
		
		//model.addAttribute("principal", principalDetails.getUsers());
		
		return "user/update";
	}
}
