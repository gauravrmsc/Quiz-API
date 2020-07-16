package com.volvain.buildout.service;

//CHECKSTYLE:OFF
import com.volvain.buildout.dto.*;
import com.volvain.buildout.repositoryservice.QuestionRepositoryService;
import com.volvain.buildout.repositoryservice.repository.entity.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//CHECKSTYLE:ON


@Service
public class QuizService {
  @Autowired
  QuestionRepositoryService questionRepositoryService;

  public GetQuestionsResponse getModuleQuestions(String module) {
    List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(module);
    GetQuestionsResponse response = new GetQuestionsResponse();
    List<GetQuestionsResponseDto> questionResponseDtos = new ArrayList<>();
    ModelMapper modelMapper = new ModelMapper();
    for (Question moduleQuestion : moduleQuestions) {
      GetQuestionsResponseDto dto = modelMapper.map(moduleQuestion, GetQuestionsResponseDto.class);
      questionResponseDtos.add(dto);
    }
    response.setQuestions(questionResponseDtos);
    return response;
  }

  public SubmitQuestionResponse validateResponse(String moduleId, SubmitQuestionRequest request) {
    Integer score = 0;
    List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(moduleId);
    HashMap<String, SubmitQuestionRequestDto> userSubmission = mapUserResponse(request);
    List<SubmitQuestionResponseDto> resultList = new ArrayList<>();
    for (Question moduleQuestion : moduleQuestions) {
      SubmitQuestionResponseDto questionResult = new SubmitQuestionResponseDto();
      addQuestionDescription(questionResult, moduleQuestion);
      SubmitQuestionRequestDto userResponseForQuestion =
          userSubmission.get(moduleQuestion.getQuestionId());
      if (userResponseForQuestion != null) {
        questionResult.setUserAnswer(userResponseForQuestion.getUserResponse());
        if (questionResult.getCorrect().equals(questionResult.getUserAnswer())) {
          questionResult.setAnswerCorrect(true);
          score += 1;
        }
      }
      resultList.add(questionResult);
    }
    SubmitQuestionResponse response = new SubmitQuestionResponse();
    response.setQuestions(resultList);
    Summary summary = new Summary(score, moduleQuestions.size());
    response.setSummary(summary);
    return response;
  }



  private void addQuestionDescription(SubmitQuestionResponseDto questionResult,
      Question moduleQuestion) {
    ModelMapper modelMapper = new ModelMapper();
    PropertyMap<Question, SubmitQuestionResponseDto> propertyMap =
        new PropertyMap<Question, SubmitQuestionResponseDto>() {
          @Override
          protected void configure() {
            map().setCorrect(source.getCorrectAnswer());
            map().setAnswerCorrect(false);
          }
        };
    modelMapper.addMappings(propertyMap);
    modelMapper.map(moduleQuestion, questionResult);
    questionResult.setCorrect(moduleQuestion.getCorrectAnswer());
  }

  HashMap<String, SubmitQuestionRequestDto> mapUserResponse(SubmitQuestionRequest request) {
    HashMap<String, SubmitQuestionRequestDto> mappedUserSubmission = new HashMap<>();
    List<SubmitQuestionRequestDto> userSubmission = request.getUserResponse();
    if (userSubmission != null) {
      for (SubmitQuestionRequestDto requestDto : userSubmission) {
        mappedUserSubmission.put(requestDto.getQuestionId(), requestDto);
      }
    }
    return mappedUserSubmission;
  }
}
