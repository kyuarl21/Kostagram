package kyu.pj.kostagram.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

//어노테이션이 없어도 JpaRepository를 상속하면 IoC등록이 자동
public interface UsersRepository extends JpaRepository<Users, Integer> {
	//JPA Query Method
	Users findByUsername(String username);
}
