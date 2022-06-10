package kyu.pj.kostagram.domain.subscribe;

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
			name = "subscribe_uk",
			columnNames = {"fromUserId", "toUserId"}
		)
	}
)
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 DB를 따라감 (Oracle은 Seq)
	private int id;
	@JoinColumn(name = "fromUserId") //강제로 칼럼명 지정
	@ManyToOne
	private Users fromUser;
	@JoinColumn(name = "toUserId")
	@ManyToOne
	private Users toUser;
	private LocalDateTime createDate;
	
	@PrePersist //DB에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
