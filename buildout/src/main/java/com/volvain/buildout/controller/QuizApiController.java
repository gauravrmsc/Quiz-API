package com.volvain.buildout.controller;

//CHECKSTYLE:OFF
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvain.buildout.dto.GetQuestionsResponse;
import com.volvain.buildout.dto.SubmitQuestionRequest;
import com.volvain.buildout.dto.SubmitQuestionResponse;
import com.volvain.buildout.service.QuizService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//CHECKSTYLE:ON


@RestController
@RequestMapping("/quiz")
public class QuizApiController {

  @Autowired
  QuizService quizService;



  @RequestMapping(value = "/{moduleId}", method = RequestMethod.GET)
  public ResponseEntity<GetQuestionsResponse> getQuestions(@PathVariable String moduleId,
      HttpServletRequest request) {
    GetQuestionsResponse response = quizService.getModuleQuestions(moduleId);
    if (response.getQuestions().size() == 0) {
      return new ResponseEntity<GetQuestionsResponse>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<GetQuestionsResponse>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/{moduleId}", method = RequestMethod.POST)
  public ResponseEntity<SubmitQuestionResponse> submitResponse(@PathVariable String moduleId,
      @RequestBody String requestData) {
    ObjectMapper mapper = new ObjectMapper();
    SubmitQuestionRequest submitQuestionRequest = null;
    if (requestData == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    try {
      submitQuestionRequest = mapper.readValue(requestData, SubmitQuestionRequest.class);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    SubmitQuestionResponse response = quizService.validateResponse(moduleId, submitQuestionRequest);
    if (response.getQuestions().size() == 0) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
