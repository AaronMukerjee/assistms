package com.portal.src.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults( level=AccessLevel.PRIVATE)
public class TrainingDataSet {
	
	String displayName;
	List<String> trainingPhrasesParts;
	List<String> messageTexts;
}
