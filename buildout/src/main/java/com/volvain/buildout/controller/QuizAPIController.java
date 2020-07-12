package com.volvain.buildout.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvain.buildout.dto.GetQuestionsResponseDTO;
import com.volvain.buildout.dto.SubmitQuestionRequest;
import com.volvain.buildout.dto.SubmitQuestionResponseDTO;
import com.volvain.buildout.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class QuizAPIController {

  @Autowired
  QuizService quizService;

  @RequestMapping("")
  public String greet() {
    return System.getProperty("greet");
  }

  @RequestMapping("/{moduleId}")
  public Map<String, List<GetQuestionsResponseDTO>> getQuestions(@PathVariable String moduleId) {
    return quizService.getModuleQuestions(moduleId).getResponse();
  }

  @RequestMapping(value = "/{moduleId}", method = RequestMethod.POST)
  public Map<String, List<SubmitQuestionResponseDTO>> submitResponse(@PathVariable String moduleId,
    @RequestBody String requestData) {
    ObjectMapper mapper = new ObjectMapper();
    SubmitQuestionRequest submitQuestionRequest = null;
    try {
      submitQuestionRequest = mapper.readValue(requestData, SubmitQuestionRequest.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return quizService.ValidateResponse(moduleId, submitQuestionRequest).getResult();
  }
}
