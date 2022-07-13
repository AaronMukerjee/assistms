package com.portal.src.exceptionshandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.portal.src.exceptions.IntentNotCreatedException;
import com.portal.src.exceptions.TrainingDataFetchException;
import com.portal.src.exceptions.TrainingDataNotInsertedException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(value = {IntentNotCreatedException.class})
	public ResponseEntity<ExceptionResponse> handleIntentCreationException(IntentNotCreatedException ex){
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage("The intent could not be created");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ExceptionResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(value = {TrainingDataFetchException.class})
	public ResponseEntity<ExceptionResponse> handleTrainingDataFetchException(TrainingDataFetchException ex){
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage("The Data could not be fetched.");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ExceptionResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	@ExceptionHandler(value = {TrainingDataNotInsertedException.class})
	public ResponseEntity<ExceptionResponse> handleTrainingDataInsertionException(TrainingDataNotInsertedException ex){
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage("The data could not be inserted");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ExceptionResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<ExceptionResponse> handleTrainingDataInsertionException(Exception ex){
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage("The generic exception occured");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ExceptionResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
