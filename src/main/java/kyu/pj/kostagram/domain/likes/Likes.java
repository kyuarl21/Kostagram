package kyu.pj.kostagram.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //DB에 테이블 생성
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			name = "likes_uk",
			columnNames = {"imageId", "userId"}
		)
	}
)
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 DB를 따라감 (Oracle은 Seq)
	private int id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne //기본이 Eager, OneToMany는 Lazy
	private Image image;
	
	@JsonIgnoreProperties({"images"}) //JSON으로 파싱 못하게 막음 (무한참조 방지)
	@JoinColumn(name = "userId")
	@ManyToOne
	private Users users;
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
