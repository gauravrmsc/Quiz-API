package com.volvain.buildout.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SubmitQuestionResponseDto {
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
