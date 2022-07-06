package com.portal.src.service;


import java.io.IOException;
import java.util.List;

import com.google.cloud.dialogflow.v2.Intent;
import com.portal.src.entity.TrainingSet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssistService {

	public Flux<TrainingSet> getAll();
	public Mono<TrainingSet> creatEntry(TrainingSet data);
	public Intent createIntent(String trainingPhrasesPart, String messageText)throws IOException ;
	public void deleteQuestionsByIds(List<String> ids);
	Intent createIntent1(String displayName,List<String> trainingPhrasesParts,List<String> messageTexts) throws IOException;
}
