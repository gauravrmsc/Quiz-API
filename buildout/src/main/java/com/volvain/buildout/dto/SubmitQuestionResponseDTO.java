package com.volvain.buildout.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubmitQuestionResponseDTO {
String questionId;
String title;
String description;
String type;
Map<String, String> options;
List<String> userAnswer;
List<String> correct;
String explanation;
boolean answerCorrect;
}
