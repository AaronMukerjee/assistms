package com.portal.src.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.portal.src.entity.TrainingSet;
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
		}
		catch(Exception ex) {
			throw ex;
		}
		
	}

	@Override
	public Mono<TrainingSet> creatEntry(TrainingSet data) {
		// TODO Auto-generated method stub
		try {
		return dataRepo.save(data);
		}
		catch(Exception ex) {
			throw ex;
		}

	}

	@Override
	public Intent createIntent(String displayName, List<String> trainingPhrasesParts,
			List<String> messageTexts) throws IOException {
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

}
