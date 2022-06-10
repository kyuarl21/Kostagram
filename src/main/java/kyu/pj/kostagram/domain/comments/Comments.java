package kyu.pj.kostagram.domain.comments;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

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
@Entity
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 DB를 따라감 (Oracle은 Seq)
	private int id;
	
	@Column(length = 100, nullable = false)
	private String content;
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Users users;
	
	@JoinColumn(name = "imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image;
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
