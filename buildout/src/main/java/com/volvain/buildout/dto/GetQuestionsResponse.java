package com.volvain.buildout.dto;

import com.volvain.buildout.repositoryService.repository.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GetQuestionsResponse {

  List<GetQuestionsResponseDTO> questionResponseDTOs;

  public Map<String, List<GetQuestionsResponseDTO>> getResponse() {
    HashMap<String, List<GetQuestionsResponseDTO>> response = new HashMap<>();
    response.put("questions", questionResponseDTOs);
    return response;
  }

}
