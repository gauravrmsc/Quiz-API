package com.volvain.buildout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitQuestionRequest {
  @JsonProperty("responses")
  List<SubmitQuestionRequestDto> userResponse;
}
