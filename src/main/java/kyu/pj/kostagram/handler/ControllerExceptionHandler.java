package kyu.pj.kostagram.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import kyu.pj.kostagram.handler.exception.CustomApiException;
import kyu.pj.kostagram.handler.exception.CustomException;
import kyu.pj.kostagram.handler.exception.CustomValidationAPIException;
import kyu.pj.kostagram.handler.exception.CustomValidationException;
import kyu.pj.kostagram.util.Script;
import kyu.pj.kostagram.web.dto.CMRespDto;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
	
	//브라우저 통신이라 스크립트로 응답
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	//Ajax통신이라 데이터로 응답
	@ExceptionHandler(CustomValidationAPIException.class)
	public ResponseEntity<?> validationAPIException(CustomValidationAPIException e) {
		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomException.class)
	public String exception(CustomException e) {
		
		return Script.back(e.getMessage());
	}
}
