package com.portal.src.exceptionshandling;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExceptionResponse {

	private String message;
	private HttpStatus status;
}
