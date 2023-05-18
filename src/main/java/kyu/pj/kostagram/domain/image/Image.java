package kyu.pj.kostagram.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kyu.pj.kostagram.domain.comment.Comment;
import kyu.pj.kostagram.domain.likes.Likes;
import kyu.pj.kostagram.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //DB에 테이블 생성
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 DB를 따라감 (Oracle은 Seq)
	private Integer id;
	private String caption; //사진 내용
	private String postImageUrl; //사진이 서버에 저장된 경로를 DB에 insert
	
	@JsonIgnoreProperties({"images"}) //JSON으로 파싱 못하게 막음 (무한참조 방지)
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.EAGER) //Image를 select하면 join해서 User정보를 같이 들고옴
	private User user;
	
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image") //mappedBy = 이 칼럼은 연관 관계의 주인이 아니니 외래키를 만들지 않음
	private List<Likes> likes; //이미지 좋아요 양방향 매핑
	
	@OrderBy("id")
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> commentList; //댓글 양방향 매핑
	
	@Transient //DB에 칼럼이 만들어지지 않음
	private boolean likeState; //좋아요가 눌린 상태인지 아닌지
	@Transient
	private int likeCount;
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	//Object를 콘솔에 출력할때 문제가 발생해서 User를 출력 안되게 함
	/*@Override
	public String toString() {
		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl
				+ ", createDate=" + createDate + "]";
	}*/
}
