package com.volvain.buildout.dto;

import lombok.Data;

import java.util.Map;

@Data
public class GetQuestionsResponseDTO {
  String questionId;
  String title;
  String type;
  Map<String, String> options;
}
