package kyu.pj.kostagram.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kyu.pj.kostagram.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //DB에 테이블 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 DB를 따라감(Oracle은 Seq)
	private Integer id;
	
	@Column(length = 100, unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	private String website; //웹사이트
	private String bio; //자기소개
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	private String profileImageUrl; //프로필 사진
	private String role; //권한
	
	//연관 관계의 주인은 = Image테이블의 user, mappedBy = 이 칼럼은 연관 관계의 주인이 아니니 외래키를 만들지 않음
	//User를 Select할때 해당 User id로 등록된 image들을 다 가져옴
	//Lazy = User를 Select할때 해당 User id로 등록된 image들을 안가져옴 -> 대신 getImages() 함수의 image들이 호출될때 가져옴
	//Eager = User를 Select할때 해당 User id로 등록된 image들을 Join해서 전부 가져옴
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //Lazy로딩
	@JsonIgnoreProperties({"user"}) //Image의 user가 JSON으로 파싱이 되는걸 막음
	private List<Image> images; //양방향 매핑
	private LocalDateTime createDate;
	
	@PrePersist //DB에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
