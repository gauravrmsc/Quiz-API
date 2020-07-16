
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.volvain.buildout.integ.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvain.buildout.integ.test.configs.AssessmentConfig;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTest {
  public static final String BASE_URL = "http://localhost:8081";
  private Map<String, AssessmentConfig> configMap;

  @BeforeAll
  public void readConfigs() throws IOException, URISyntaxException {
    ObjectMapper mapper = new ObjectMapper();
    AssessmentConfig[] assessmentConfigs =
        mapper.readValue(resolveFileAsString("assessments.json"), AssessmentConfig[].class);
    configMap = Stream.of(assessmentConfigs)
        .collect(Collectors.toMap(AssessmentConfig::getName, config -> config));
  }

  private void executeTest(String name) throws Exception {
    AssessmentConfig config = configMap.get(name);
    switch (config.getMethod()) {
      case "GET":
        executeGetRequest(config);
        break;
      case "POST":
        executePost(config);
        break;
      default:
        //executePut(config);
        break;
    }
  }


  public void executeTests() throws IOException, URISyntaxException {
    ObjectMapper mapper = new ObjectMapper();
    AssessmentConfig[] assessmentConfigs =
        mapper.readValue(resolveFileAsString("assessments.json"), AssessmentConfig[].class);
    List<Exception> result = Stream.of(assessmentConfigs).map(config -> {
      try {
        executeTest(config.getName());
      } catch (Exception ex) {
        ex.printStackTrace();
        return ex;
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
    Assertions.assertTrue(result.isEmpty(), "All test-cases should have passed.");
  }


  public void executePut(AssessmentConfig config) throws Exception {
    URI uri = UriComponentsBuilder.fromPath(config.getUrl()).build().toUri();

    String content = resolveFileAsString(config.getInput());

    WebClient client = WebClient.builder().baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

    String response = client.put().uri(BASE_URL + uri).bodyValue(content).exchange().block()
        .bodyToMono(String.class).block();

    Assertions.assertNull(response);
  }

  private String resolveFileAsString(String input) throws URISyntaxException, IOException {
    File inputFile =
        new File(Thread.currentThread().getContextClassLoader().getResource(input).toURI());
    return new String(Files.readAllBytes(inputFile.toPath()), "utf-8");
  }

  public void executeGetRequest(AssessmentConfig config) throws Exception {
    URI uri = UriComponentsBuilder.fromPath(config.getUrl()).build().toUri();

    String content = resolveFileAsString(config.getResponse());

    WebClient client = WebClient.builder().baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

    String response =
        client.get().uri(BASE_URL + uri).exchange().block().bodyToMono(String.class).block();

    System.out.println(response);
    //JSONAssert.assertEquals(content, response, false);
  }


  public void executePost(AssessmentConfig config) throws Exception {
    URI uri = UriComponentsBuilder.fromPath(config.getUrl()).build().toUri();

    String requestContent = resolveFileAsString(config.getInput());
    String responseContent = resolveFileAsString(config.getResponse());

    WebClient client = WebClient.builder().baseUrl(BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

    String response = client.post().uri(BASE_URL + uri).bodyValue(requestContent).exchange().block()
        .bodyToMono(String.class).block();

    System.out.println(response);
    //JSONAssert.assertEquals(responseContent, response, false);
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    IntegrationTest test = new IntegrationTest();
    test.readConfigs();
    test.executeTests();
  }

}
