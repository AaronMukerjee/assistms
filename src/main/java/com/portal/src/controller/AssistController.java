package com.portal.src.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.dialogflow.v2.Intent;
import com.portal.src.dto.TrainingDataSet;
import com.portal.src.entity.TrainingSet;
import com.portal.src.service.AssistService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class AssistController {
	 final AssistService service;
	 
		@GetMapping("/getAll")
	public ResponseEntity<Flux<TrainingSet>> getAllQuestions(){
		return new ResponseEntity(service.getAll(),HttpStatus.OK);
	}
		@PostMapping("/")
		public Mono<TrainingSet> insertData(@RequestBody TrainingSet data){
			return service.creatEntry(data);
		}
		@PostMapping("/Intent")
		public Intent createIntent(@RequestBody TrainingDataSet data) throws IOException {
			return service.createIntent(data.getDisplayName(), data.getTrainingPhrasesParts(), data.getMessageTexts());
		}
}