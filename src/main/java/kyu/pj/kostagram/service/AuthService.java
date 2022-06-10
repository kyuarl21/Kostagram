package kyu.pj.kostagram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service //1. IoC 2. 트랜잭션 관리
public class AuthService {
	
	private final UsersRepository usersRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional //트랜잭션 관리
	public Users signUp(Users users) { //외부의 user정보
		//회원가입 진행
		String rawPassword = users.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		users.setPassword(encPassword);
		users.setRole("ROLE_USER"); //관리자는 ROLE_ADMIN
		Users usersEntity = usersRepository.save(users); //DB의 user정보
		
		return usersEntity;
	}
}
