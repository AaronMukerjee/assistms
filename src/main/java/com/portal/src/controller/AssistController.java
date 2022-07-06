package com.portal.src.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.dialogflow.v2.Intent;
import com.portal.src.dto.IntentRequestBody;
import com.portal.src.dto.TrainingDataSet;
import com.portal.src.dto.TrainingDataSetV2;
import com.portal.src.entity.TrainingSet;
import com.portal.src.exceptions.DataNotDeletedException;
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
	@CrossOrigin(origins = "*")
	@CircuitBreaker(fallbackMethod = "getAllQuestionsFallBack", name = "DataFetchFallback")
	public ResponseEntity<Flux<TrainingSet>> getAllQuestions() {
		try {
			logger.info("Calling the service for fetching data");
			return new ResponseEntity(service.getAll(), HttpStatus.OK);
		} catch (RuntimeException ex) {
			// TODO Auto-generated catch block
			logger.info(
					"The following exception occured while fetching training data from dB" + ex.getLocalizedMessage());
			TrainingDataFetchException e = new TrainingDataFetchException();
			e.initCause(ex);
			throw e;
		}
	}

	public ResponseEntity<Flux<TrainingSet>> getAllQuestionsFallBack() {
		logger.info("Inside the fall back method of getAllQuestions");
		return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/")
	@CrossOrigin(origins = "*")
	@CircuitBreaker(fallbackMethod = "insertDataFallBackMethod", name = "InsertDataFallback")
	public ResponseEntity<Mono<TrainingSet>> insertData(@RequestBody TrainingSet data) {
		try {
			return new ResponseEntity(service.creatEntry(data), HttpStatus.OK);
		} catch (RuntimeException ex) {
			// TODO Auto-generated catch block
			logger.info(
					"The following exception occured while inserting training data in DB " + ex.getLocalizedMessage());
			IntentNotCreatedException e = new IntentNotCreatedException();
			e.initCause(ex);
			throw e;
		}
	}

	public ResponseEntity<Mono<TrainingSet>> insertDataFallBackMethod(@RequestBody TrainingSet data) {
		logger.info("Inside the fall back method of Insert Data");
		return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping("/Intent")
	@CrossOrigin(origins = "*")
	@CircuitBreaker(fallbackMethod = "createIntentFallBackMethod", name = "CreateIntentFallback")
	public ResponseEntity<String> createIntent(@RequestBody TrainingDataSet data) throws IOException {
		try {
			service.createIntent(data.getTrainingPhrasesParts(), data.getMessageTexts());
			return new ResponseEntity<String>("Data was inserted successfully", HttpStatus.CREATED);
		} catch (RuntimeException ex) {
			// TODO Auto-generated catch block
			logger.info("The following exception occured while inserting new Intent " + ex.getLocalizedMessage());
			TrainingDataNotInsertedException e = new TrainingDataNotInsertedException();
			e.initCause(ex);
			throw e;
		}
	}

	public ResponseEntity<String> createIntentFallBackMethod(@RequestBody TrainingDataSet data) throws IOException {
		logger.info("Inside the fall back method of create Intent");
		return new ResponseEntity<String>("Data was could not be inserted", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@DeleteMapping
	@CrossOrigin(origins = "*")
	@CircuitBreaker(fallbackMethod = "deleteQuestionsFallBackMethod", name = "DeleteIntentFallback")
	public ResponseEntity<String> deleteQuestions(@RequestBody ArrayList<String> ids) throws IOException {
		try {
			service.deleteQuestionsByIds(ids);
			return new ResponseEntity<String>("Data was deleted successfully", HttpStatus.OK);
		} catch (Exception ex) {
			logger.info("The following exception occured while deleting data" + ex.getLocalizedMessage());
			DataNotDeletedException e = new DataNotDeletedException();
			e.initCause(ex);
			throw e;
		}
	}

	public ResponseEntity<String> deleteQuestionsFallBackMethod(@RequestBody ArrayList<String> ids) throws IOException {
		logger.info("Inside the fall back method of Insert Data");
		return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping("/Intent1")
	public ResponseEntity<String> createIntentV2(@RequestBody TrainingDataSetV2 data) throws IOException {
		try {
			service.createIntent1(data.getDisplayName(),data.getTrainingPhrasesParts(),data.getMessages());
			return new ResponseEntity<String>("Data was inserted successfully", HttpStatus.CREATED);
		} catch (RuntimeException ex) {
			// TODO Auto-generated catch block
			logger.info("The following exception occured while inserting new Intent " + ex.getLocalizedMessage());
			TrainingDataNotInsertedException e = new TrainingDataNotInsertedException();
			e.initCause(ex);
			throw e;
		}
	}
	
	
	
	
	
	
	
	
	/*
	 * @PostMapping("/Intent/v2") public ResponseEntity<Intent>
	 * createIntent(@RequestBody IntentRequestBody data) throws IOException { try {
	 * logger.info("Calling the service for creating the intent"); return new
	 * ResponseEntity(service.createIntent1(data.getIntentName(),
	 * data.getIds(),data.getMessage()), HttpStatus.OK); } catch (RuntimeException
	 * ex) { // TODO Auto-generated catch block logger.info(
	 * "The following exception occured while fetching training data from dB" +
	 * ex.getLocalizedMessage()); TrainingDataFetchException e = new
	 * TrainingDataFetchException(); e.initCause(ex); throw e; } }
	 */
}