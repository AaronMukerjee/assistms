package com.portal.src.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.src.dto.TrainingDataSet;
import com.portal.src.entity.TrainingSet;
import com.portal.src.exceptions.IntentNotCreatedException;
import com.portal.src.exceptions.TrainingDataFetchException;
import com.portal.src.exceptions.TrainingDataNotInsertedException;
import com.portal.src.service.AssistService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AssistController {
	final AssistService service;
	public static Logger logger = LoggerFactory.getLogger(AssistController.class);
	@GetMapping("/")
	@CircuitBreaker(fallbackMethod = "getAllQuestionsFallBack", name = "DataFetchFallback")
	public ResponseEntity<Flux<TrainingSet>> getAllQuestions() {
		try {
			logger.info("Calling the service for fetching data");
			return new ResponseEntity(service.getAll(),HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			logger.info("The following exception occured while fetching training data from dB"+e.getLocalizedMessage() );
			throw new TrainingDataFetchException();
		}
	}
	public ResponseEntity<Flux<TrainingSet>> getAllQuestionsFallBack() {
			logger.info("Inside the fall back method of getAllQuestions");
			return new ResponseEntity(null,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/")
	@CircuitBreaker(fallbackMethod = "insertDataFallBackMethod", name = "InsertDataFallback")
	public ResponseEntity<Mono<TrainingSet>>insertData(@RequestBody TrainingSet data) {
		try {
			return new ResponseEntity(service.creatEntry(data),HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			logger.info("The following exception occured while inserting training data in DB "+e.getLocalizedMessage() );
			throw new IntentNotCreatedException();
		}
	}
	public ResponseEntity<Mono<TrainingSet>>insertDataFallBackMethod(@RequestBody TrainingSet data) {
		logger.info("Inside the fall back method of Insert Data");
			return new ResponseEntity(null,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@PostMapping("/Intent")
	@CircuitBreaker(fallbackMethod = "createIntentFallBackMethod", name = "CreateIntentFallback")
	public ResponseEntity<String> createIntent(@RequestBody TrainingDataSet data) throws IOException {
		try {
		 service.createIntent(data.getDisplayName(), data.getTrainingPhrasesParts(), data.getMessageTexts());
		 return new ResponseEntity<String>("Data was inserted successfully",HttpStatus.CREATED);
		}catch (RuntimeException e) {
			// TODO Auto-generated catch block
			logger.info("The following exception occured while inserting new Intent "+e.getLocalizedMessage() );
			throw new TrainingDataNotInsertedException();
		}
	}
	public ResponseEntity<String> createIntentFallBackMethod(@RequestBody TrainingDataSet data) throws IOException {
		logger.info("Inside the fall back method of create Intent");
		return new ResponseEntity<String>("Data was could not be fetched",HttpStatus.CREATED);
		
	}
}