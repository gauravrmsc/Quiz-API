package com.volvain.buildout.dto;

import java.util.List;
import lombok.Data;

@Data
public class SubmitQuestionResponse {
  List<SubmitQuestionResponseDto> questions;
  Summary summary;
}
