package com.volvain.buildout.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubmitQuestionResponse {
  Map<String, List<SubmitQuestionResponseDTO>> result;
}
