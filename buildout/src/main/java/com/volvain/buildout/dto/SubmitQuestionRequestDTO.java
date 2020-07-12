package com.volvain.buildout.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubmitQuestionRequestDTO {
  String questionId;
  List<String> userResponse;
}
