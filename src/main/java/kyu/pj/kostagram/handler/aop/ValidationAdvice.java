package kyu.pj.kostagram.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import kyu.pj.kostagram.handler.exception.CustomValidationAPIException;
import kyu.pj.kostagram.handler.exception.CustomValidationException;

@Component //RestController, Service 모든 것들이 Component를 상속해서 만들어져 있음
@Aspect
public class ValidationAdvice {
	
	//ApiController의 메소드가 실행될때 해당 메소드의 모든것에 접근이 가능
	@Around("execution(* kyu.pj.kostagram.web.api.*Controller.*(..))")
	public Object ApiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					
					throw new CustomValidationAPIException("유효성검사 실패", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
	
	//Controller의 메소드가 실행될때 해당 메소드의 모든것에 접근이 가능
	@Around("execution(* kyu.pj.kostagram.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
					}
					
					throw new CustomValidationException("유효성검사 실패", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
