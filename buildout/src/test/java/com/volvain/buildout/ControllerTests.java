package com.volvain.buildout;

//CHECKSTYLE:OFF
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvain.buildout.dto.SubmitQuestionRequest;
import com.volvain.buildout.dto.SubmitQuestionRequestDto;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {BuildOutApplication.class})
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ControllerTests {
  @Autowired
  private MockMvc mockMvc;
  private static final String QUIZ_API_URI = "/quiz";
  private static final String BASE_URI = "http://localhost:8081/";
  private static final int MODULE = 1;
  private static final SubmitQuestionRequestDto question1Response =
      new SubmitQuestionRequestDto("001", Arrays.asList("4"));
  private static final SubmitQuestionRequestDto question2Response =
      new SubmitQuestionRequestDto("002", Arrays.asList("1", "3", "4"));
  private static final SubmitQuestionRequestDto question3Response =
      new SubmitQuestionRequestDto("003", Arrays.asList("throwable"));
  private static final SubmitQuestionRequestDto incorectQuestion3Response =
      new SubmitQuestionRequestDto("003", Arrays.asList("throw"));


  @Test
  public void sendValidGetQuestionsRequestGetAllQuestions() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/" + MODULE).build().toUri();
    MockHttpServletResponse response = mockMvc.perform(get(uri)).andReturn().getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assert (response.getContentAsString() != null);
    assert (!response.getContentAsString().equals(""));
  }

  @Test
  public void noModuleParameterReturnNotFound() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/").build().toUri();
    MockHttpServletResponse response =
        mockMvc.perform(get(uri.toString())).andReturn().getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    response = mockMvc.perform(post(uri)).andReturn().getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void invalidModuleReturnNotFound() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/a").build().toUri();
    MockHttpServletResponse response =
        mockMvc.perform(get(uri.toString())).andReturn().getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    SubmitQuestionRequest responses = new SubmitQuestionRequest(
        Arrays.asList(question2Response, incorectQuestion3Response, question1Response));
    String requestString = new ObjectMapper().writeValueAsString(responses);
    response = mockMvc.perform(post(uri).content(requestString)).andReturn().getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void invalidURLReturnNotFound() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI).build().toUri();
    MockHttpServletResponse response =
        mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn().getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void requestToValidateResponseWithNullBodyReturnBadRequest() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/1").build().toUri();
    MockHttpServletResponse response = mockMvc.perform(post(uri)).andReturn().getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void sendValidSubmitRequestGetCorrectAssessment() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/1").build().toUri();
    SubmitQuestionRequest responses = new SubmitQuestionRequest(
        Arrays.asList(question1Response, question2Response, question3Response));
    String requestString = new ObjectMapper().writeValueAsString(responses);
    MockHttpServletResponse response =
        mockMvc.perform(post(uri).content(requestString)).andReturn().getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    String responseBody = response.getContentAsString();
    assert (responseBody.contains("true"));
    assert (!responseBody.contains("false"));
  }

  @Test
  public void sendIncorrectChoiceGetFalseAsISCorrect() throws Exception {
    URI uri = UriComponentsBuilder.fromPath(QUIZ_API_URI + "/1").build().toUri();
    SubmitQuestionRequest responses = new SubmitQuestionRequest(
        Arrays.asList(question2Response, incorectQuestion3Response, question1Response));
    String requestString = new ObjectMapper().writeValueAsString(responses);
    MockHttpServletResponse response =
        mockMvc.perform(post(uri).content(requestString)).andReturn().getResponse();
    String responseBody = response.getContentAsString();
    assert (responseBody.contains("true"));
    assert (responseBody.contains("false"));
    assert (responseBody.indexOf("true") != responseBody.lastIndexOf("true"));
  }
}
