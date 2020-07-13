package com.volvain.buildout.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitQuestionRequestDto {
  String questionId;
  List<String> userResponse;
}
