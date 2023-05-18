package kyu.pj.kostagram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kyu.pj.kostagram.domain.comment.Comment;
import kyu.pj.kostagram.domain.comment.CommentRepository;
import kyu.pj.kostagram.domain.image.Image;
import kyu.pj.kostagram.domain.user.User;
import kyu.pj.kostagram.domain.user.UserRepository;
import kyu.pj.kostagram.handler.exception.CustomApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment writeComment(String content, int imageId, int userId) {
		
		//id값만 필요해서 id값만 담을 객체를 간단하게 만듬
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void deleteComment(int id) {
		
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}
