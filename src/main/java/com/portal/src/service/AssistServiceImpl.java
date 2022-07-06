package com.portal.src.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.cloud.dialogflow.v2.AgentName;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.Intent.Message;
import com.google.cloud.dialogflow.v2.Intent.Message.Text;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part;
import com.google.cloud.dialogflow.v2.IntentsClient;
import com.google.common.base.Optional;
import com.portal.src.dto.TrainingDataSet;
import com.portal.src.entity.TrainingSet;
import com.portal.src.exceptions.IntentNotCreatedException;
import com.portal.src.repository.TrainingDataRepo;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AssistServiceImpl implements AssistService {
	final TrainingDataRepo dataRepo;
	@Value("${spring.cloud.gcp.firestore.project-id}")
	String projectId;

	@Override
	public Flux<TrainingSet> getAll() {
		try {
			return dataRepo.findAll();
		} catch (Exception ex) {
			throw ex;
		}

	}

	@Override
	public Mono<TrainingSet> creatEntry(TrainingSet data) {
		// TODO Auto-generated method stub
		try {
			return dataRepo.save(data);
		} catch (Exception ex) {
			throw ex;
		}

	}

	@Override
	public Intent createIntent1(String displayName,List<String> trainingPhrasesParts,List<String> messageTexts) throws IOException {
	
	  // Instantiates a client
		if(trainingPhrasesParts==null) {
			trainingPhrasesParts= Arrays.asList("");
		}
	  try (IntentsClient intentsClient = IntentsClient.create()) {
	    // Set the project agent name using the projectID (my-project-id)
	    AgentName parent = AgentName.of(projectId);

	    // Build the trainingPhrases from the trainingPhrasesParts
	    List<TrainingPhrase> trainingPhrases = new ArrayList<>();
	    for (String trainingPhrase : trainingPhrasesParts) {
	      trainingPhrases.add(
	          TrainingPhrase.newBuilder()
	              .addParts(Part.newBuilder().setText(trainingPhrase).build())
	              .build());
	    }

	    // Build the message texts for the agent's response
	    Message message =
	        Message.newBuilder().setText(Text.newBuilder().addAllText(messageTexts).build()).build();

	    // Build the intent
	    Intent intent =
	        Intent.newBuilder()
	            .setDisplayName(displayName)
	            .addMessages(message)
	            .addAllTrainingPhrases(trainingPhrases)
	            .build();

	    // Performs the create intent request
	    Intent response = intentsClient.createIntent(parent, intent);
	    System.out.format("Intent created: %s\n", response);

	    return response;
	  }
		/*
		 * try (IntentsClient intentsClient = IntentsClient.create()) {
		 * List<TrainingPhrase> trainingPhrases = new ArrayList<>(); AgentName parent =
		 * AgentName.of(projectId); Flux<TrainingSet> monoQuestionsList =
		 * dataRepo.findAllById(ids); monoQuestionsList.subscribe(TrainingData -> {
		 * createIntentUsingEntity(TrainingData, trainingPhrases); }); Message
		 * messageResponse = Message.newBuilder()
		 * .setText(Text.newBuilder().addAllText(Arrays.asList(message)).build()).build(
		 * ); Intent intent =
		 * Intent.newBuilder().setDisplayName(intentName).addMessages(messageResponse)
		 * .addAllTrainingPhrases(trainingPhrases).build(); Optional<Intent> response =
		 * Optional.of(intentsClient.createIntent(parent, intent)); if
		 * (response.isPresent()) { deleteQuestionsByIds(ids);
		 * System.out.format("Intent created: %s\n", response); return response.get(); }
		 * else { throw new IntentNotCreatedException(); }
		 * 
		 * }
		 */
	}

	private Intent createIntentUsingEntity(TrainingSet data, List<TrainingPhrase> trainingPhrases) {
		trainingPhrases.add(
				TrainingPhrase.newBuilder().addParts(Part.newBuilder().setText(data.getQuestion()).build()).build());

		Message.newBuilder().setText(Text.newBuilder().addText(data.getAnswer()).build()).build();
		return null;
	}

	@Override
	public Intent createIntent(String trainingPhrasesPart, String messageText)
			throws IOException {
		
		List<String> trainingPhrasesParts = Arrays.asList(trainingPhrasesPart);
		List<String> messageTexts = Arrays.asList(messageText);
		String displayName = System.currentTimeMillis()+"_Intent";
		// TODO Auto-generated method stub
		// Instantiates a client

		try (IntentsClient intentsClient = IntentsClient.create()) {
			// Set the project agent name using the projectID (my-project-id)
			AgentName parent = AgentName.of(projectId);

			// Build the trainingPhrases from the trainingPhrasesParts
			List<TrainingPhrase> trainingPhrases = new ArrayList<>();
			for (String trainingPhrase : trainingPhrasesParts) {
				trainingPhrases.add(TrainingPhrase.newBuilder()
						.addParts(Part.newBuilder().setText(trainingPhrase).build()).build());
			}

			// Build the message texts for the agent's response
			Message message = Message.newBuilder().setText(Text.newBuilder().addAllText(messageTexts).build()).build();

			// Build the intent
			Intent intent = Intent.newBuilder().setDisplayName(displayName).addMessages(message)
					.addAllTrainingPhrases(trainingPhrases).build();

			// Performs the create intent request
			Intent response = intentsClient.createIntent(parent, intent);
			System.out.format("Intent created: %s\n", response);

			return response;
		}
	}

	@Override
	public void deleteQuestionsByIds(List<String> ids) {
		// TODO Auto-generated method stub

		dataRepo.deleteAll(ids.stream().map(this::creatEntity).collect(Collectors.toList())).subscribe();
		// dataRepo.delete(ids).subscribe();
		/*
		 * ids.removeAll(Collections.singleton(null)); ArrayList<String>
		 * transformedIdsList = new ArrayList<>(ids.size()); ids.forEach(i -> {
		 * transformedIdsList.add(i); }); // dataRepo.deleteAllById(transformedIdsList);
		 * 
		 * ids.forEach(i -> { dataRepo.deleteById(i).subscribe(); });
		 */

	}

	private TrainingSet creatEntity(String id) {
		TrainingSet data = new TrainingSet();
		data.setTrainingSetId(id);
		return data;
	}

}
