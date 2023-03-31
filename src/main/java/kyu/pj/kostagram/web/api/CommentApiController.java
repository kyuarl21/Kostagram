package kyu.pj.kostagram.web.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kyu.pj.kostagram.config.auth.PrincipalDetails;
import kyu.pj.kostagram.domain.comments.Comments;
import kyu.pj.kostagram.service.CommentsService;
import kyu.pj.kostagram.web.dto.CMRespDto;
import kyu.pj.kostagram.web.dto.comments.CommentsDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
	
	private final CommentsService commentsService;
	
	@PostMapping("/api/comment")
	public ResponseEntity<?> commentSave(@Valid @RequestBody CommentsDto commentsDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){
		
		Comments comments = commentsService.writeComment(commentsDto.getContent(), commentsDto.getImageId(), principalDetails.getUsers().getId());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글이 작성되었습니다", comments), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable int id){
		
		commentsService.deleteComment(id);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글이 삭제되었습니다", null), HttpStatus.OK);
	}
}
