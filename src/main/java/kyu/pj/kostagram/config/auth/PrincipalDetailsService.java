package kyu.pj.kostagram.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service //IoC
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UsersRepository usersRepository;
	
	//1. 패스워드는 알아서 체킹
	//2. return이 성공하면 자동으로 UserDetails Type을 session으로 만듬
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users usersEntity = usersRepository.findByUsername(username);
		
		if(usersEntity == null) {
			
			return null;
		}else {
			
			return new PrincipalDetails(usersEntity);
		}
	}

}
