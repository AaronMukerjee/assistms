package com.portal.src.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntentRequestBody {
	private String intentName;
	private String message;
	private List<String> ids;
}
