package com.volvain.buildout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubmitQuestionRequest {
  @JsonProperty("responses")
  List<SubmitQuestionRequestDTO> userResponse;
}
