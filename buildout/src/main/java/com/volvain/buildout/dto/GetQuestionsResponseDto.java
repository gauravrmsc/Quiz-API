package com.volvain.buildout.dto;

import java.util.Map;
import lombok.Data;

@Data
public class GetQuestionsResponseDto {
  String questionId;
  String title;
  String type;
  Map<String, String> options;
}
