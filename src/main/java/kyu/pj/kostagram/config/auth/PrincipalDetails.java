package kyu.pj.kostagram.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kyu.pj.kostagram.domain.users.Users;
import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
	
	private static final long serialVersionUID = 1L;
	
	private Users users;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(Users users) {
		this.users = users;
	}
	
	public PrincipalDetails(Users users, Map<String, Object> attributes) {
		this.users = users;
	}
	
	//권한은 한개가 아닐 수 있음 (컬렉션 타입)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add(() -> {
			
			return users.getRole();
		});
		
		return collector;
	}

	@Override
	public String getPassword() {
		
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		
		return users.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}

	@Override
	public String getName() {
		
		return (String)attributes.get("name");
	}
	
}
