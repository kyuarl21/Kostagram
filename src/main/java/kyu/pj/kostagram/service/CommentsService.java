package kyu.pj.kostagram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.domain.comments.Comments;
import kyu.pj.kostagram.domain.comments.CommentsRepository;
import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.domain.users.Users;
import kyu.pj.kostagram.domain.users.UsersRepository;
import kyu.pj.kostagram.handler.exception.CustomApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentsService {
	
	private final CommentsRepository commentsRepository;
	private final UsersRepository usersRepository;
	
	@Transactional
	public Comments writeComment(String content, int imageId, int userId) {
		
		//id값만 필요해서 id값만 담을 객체를 간단하게 만듬
		Image image = new Image();
		image.setId(imageId);
		
		Users usersEntity = usersRepository.findById(userId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다");
		});
		
		Comments comments = new Comments();
		comments.setContent(content);
		comments.setImage(image);
		comments.setUsers(usersEntity);
		
		return commentsRepository.save(comments);
	}
	
	@Transactional
	public void deleteComment(int id) {
		
		try {
			commentsRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}
