package com.volvain.buildout;
//CHECKSTYLE:OFF
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvain.buildout.dto.*;
import com.volvain.buildout.repositoryservice.QuestionRepositoryService;
import com.volvain.buildout.repositoryservice.repository.entity.Question;
import com.volvain.buildout.service.QuizService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@SpringBootTest(classes = {BuildOutApplication.class})
@ExtendWith(MockitoExtension.class)
public class ServiceLayerTest {

  @MockBean
  QuestionRepositoryService questionRepositoryService;
  @InjectMocks
  QuizService quizService;

  ObjectMapper mapper;
  Question question1;
  Question question2;
  List<GetQuestionsResponseDto> questionDTOs;
  SubmitQuestionRequest userResponse;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
    mapper = new ObjectMapper();
  }

  private void initQuestions() {
    question1 = new Question("001", "1", "What is the parent class/interface of Exception class?",
        "java Question", "Subjective", null, Arrays.asList("throwable"), null);
    HashMap<String, String> options = new HashMap<>();
    options.put("1", "0.0.0.0");
    options.put("2", "192.168.1.0");
    options.put("3", "127.0.0.0");
    options.put("4", "255.255.255.255");
    question2 =
        new Question("002", "1", "What is the default IP address of localhost?", "General Question",
            "Objective-single", options, Arrays.asList("3"), null);
    questionDTOs = new ArrayList<>();
    ModelMapper modelMapper = new ModelMapper();
    questionDTOs.add(modelMapper.map(question1, GetQuestionsResponseDto.class));
    questionDTOs.add(modelMapper.map(question1, GetQuestionsResponseDto.class));
  }

  private void setUserResponse() {
    SubmitQuestionRequestDto question1Response =
        new SubmitQuestionRequestDto("001", Arrays.asList("throwable"));
    SubmitQuestionRequestDto question2Response =
        new SubmitQuestionRequestDto("002", Arrays.asList("2"));
    userResponse = new SubmitQuestionRequest(Arrays.asList(question1Response, question2Response));
  }

  @Test
  public void validGetQuestionRequestGenerateResponse() {
    initQuestions();
    when(questionRepositoryService.getModuleQuestions("1"))
        .thenReturn(Arrays.asList(question1, question2));
    List<GetQuestionsResponseDto> fetchedDTOs = quizService.getModuleQuestions("1").getQuestions();
    assert (fetchedDTOs.equals(fetchedDTOs));
  }

  @Test
  public void invalidModuleReturnsEmptyResponse() {
    when(questionRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(new ArrayList<Question>());
    int noOfFetchedQuestions = quizService.getModuleQuestions("5").getQuestions().size();
    assertEquals(0, noOfFetchedQuestions);
    int size = quizService.validateResponse("5", new SubmitQuestionRequest()).getQuestions().size();
    assertEquals(0, size);
  }

  @Test
  public void validateRequestWithEmptyBodyGeneratesReturnsFalseForAllQuestions() {
    initQuestions();
    when(questionRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(Arrays.asList(question1, question2));
    List<SubmitQuestionResponseDto> validationResult =
        quizService.validateResponse("5", new SubmitQuestionRequest()).getQuestions();
    for (SubmitQuestionResponseDto questionResponseDTO : validationResult) {
      assertEquals(false, questionResponseDTO.isAnswerCorrect());
    }
  }

  @Test
  public void checkValidResultGenerated() {
    initQuestions();
    setUserResponse();
    when(questionRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(Arrays.asList(question1, question2));
    SubmitQuestionResponse validationResult = quizService.validateResponse("1", userResponse);
    assertEquals(true, validationResult.getQuestions().get(0).isAnswerCorrect());
    assertEquals(false, validationResult.getQuestions().get(1).isAnswerCorrect());
  }

}
