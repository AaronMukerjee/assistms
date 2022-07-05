package com.portal.src.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults( level=AccessLevel.PRIVATE)
public class TrainingDataSet {
	
	String trainingPhrasesParts;
	String messageTexts;
}
