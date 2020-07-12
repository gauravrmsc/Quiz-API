package com.volvain.buildout.service;

import com.volvain.buildout.dto.*;
import com.volvain.buildout.repositoryService.QuestionRepositoryService;
import com.volvain.buildout.repositoryService.repository.entity.Question;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class QuizService {
  @Autowired
  QuestionRepositoryService questionRepositoryService;

  public GetQuestionsResponse getModuleQuestions(String module) {
    List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(module);
    GetQuestionsResponse response = new GetQuestionsResponse();
    List<GetQuestionsResponseDTO> questionResponseDTOs = new ArrayList<>();
    ModelMapper modelMapper = new ModelMapper();
    for (Question moduleQuestion : moduleQuestions) {
      GetQuestionsResponseDTO dto = modelMapper.map(moduleQuestion, GetQuestionsResponseDTO.class);
      questionResponseDTOs.add(dto);
    }
    //modelMapper.map(moduleQuestions, new TypeToken<List<GetQuestionsResponseDTO>>() {
    //});
    response.setQuestionResponseDTOs(questionResponseDTOs);
    return response;
  }

  public SubmitQuestionResponse ValidateResponse(String moduleId, SubmitQuestionRequest request) {
    List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(moduleId);
    HashMap<String, SubmitQuestionRequestDTO> userSubmission = mapUserResponse(request);
    List<SubmitQuestionResponseDTO> resultList = new ArrayList<>();
    for (Question moduleQuestion : moduleQuestions) {
      SubmitQuestionResponseDTO questionResult = new SubmitQuestionResponseDTO();
      addQuestionDescription(questionResult, moduleQuestion);
      SubmitQuestionRequestDTO userResponseForQuestion =
        userSubmission.get(moduleQuestion.getQuestionId());
      if (userResponseForQuestion != null) {
        questionResult.setUserAnswer(userResponseForQuestion.getUserResponse());
        if (questionResult.getCorrect().equals(questionResult.getUserAnswer())) {
          questionResult.setAnswerCorrect(true);
        }
      }
      resultList.add(questionResult);
    }
    SubmitQuestionResponse validationResult = prepareValidationResult(resultList);
    return validationResult;
  }

  private SubmitQuestionResponse prepareValidationResult(
    List<SubmitQuestionResponseDTO> resultList) {
    HashMap<String, List<SubmitQuestionResponseDTO>> result = new HashMap<>();
    result.put("questions", resultList);
    SubmitQuestionResponse validationResult = new SubmitQuestionResponse();
    validationResult.setResult(result);
    return validationResult;
  }

  private void addQuestionDescription(SubmitQuestionResponseDTO questionResult,
    Question moduleQuestion) {
    ModelMapper modelMapper = new ModelMapper();
    PropertyMap<Question, SubmitQuestionResponseDTO> propertyMap =
      new PropertyMap<Question, SubmitQuestionResponseDTO>() {
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

  HashMap<String, SubmitQuestionRequestDTO> mapUserResponse(SubmitQuestionRequest request) {
    HashMap<String, SubmitQuestionRequestDTO> mappedUserSubmission = new HashMap<>();
    List<SubmitQuestionRequestDTO> userSubmission = request.getUserResponse();
    if (userSubmission != null) {
      for (SubmitQuestionRequestDTO requestDTO : userSubmission) {
        mappedUserSubmission.put(requestDTO.getQuestionId(), requestDTO);
      }
    }
    return mappedUserSubmission;
  }
}
