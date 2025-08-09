package com.example.splearn.adapter;

import com.example.splearn.domain.member.DuplicateEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ProblemDetail globalExceptionHandler(Exception e) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		problemDetail.setProperty("exception", e.getClass().getSimpleName());
		problemDetail.setProperty("timestamp", System.currentTimeMillis());

		return problemDetail;
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ProblemDetail emailExceptionHandler(DuplicateEmailException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());

		problemDetail.setProperty("exception", e.getClass().getSimpleName());
		problemDetail.setProperty("timestamp", System.currentTimeMillis());

		return problemDetail;
	}
}
