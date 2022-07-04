package com.portal.src.entity;

import org.springframework.cloud.gcp.data.firestore.Document;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.Data;

@Document
@Data
public class TrainingSet {

	@DocumentId
	private String trainingSetId;
	private String question;
	private String answer;
}
