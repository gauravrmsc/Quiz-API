package com.volvain.buildout.dto;

import java.util.List;
import lombok.Data;

@Data
public class GetQuestionsResponse {
  List<GetQuestionsResponseDto> questions;
}
