package com.volvain.buildout;
//CHECKSTYLE:OFF
import com.volvain.buildout.repositoryservice.QuestionRepositoryService;
import com.volvain.buildout.repositoryservice.repository.entity.Question;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BuildOutApplication.class})
public class RepositoryTest {

  @Autowired
  QuestionRepositoryService questionRepositoryService;
  Question question1;
  Question question2;

  @BeforeEach
  public void init() {
    question1 = new Question("001", "1", "What is the parent class/interface of Exception class?",
        "java Question", "Subjective", null, Arrays.asList("throwable"), null);
    HashMap<String, String> options = new HashMap<>();
    options.put("1", "0.0.0.0");
    options.put("2", "192.168.1.0");
    options.put("3", "127.0.0.1");
    options.put("4", "255.255.255.255");
    question2 =
        new Question("002", "1", "What is the default IP address of localhost?", "General Question",
            "Objective-single", options, Arrays.asList("3"), null);
  }

  @Test
  public void clearOperationEmptiesDatabase() {
    questionRepositoryService.clearDatabase();
    assertEquals(0, questionRepositoryService.getModuleQuestions("1").size());
  }

  @Test
  public void populateDatabaseInsertsItemsToDatabase() {
    questionRepositoryService.clearDatabase();
    questionRepositoryService.populateDatabase(Arrays.asList(question1, question2));
    assertEquals(2, questionRepositoryService.getModuleQuestions("1").size());
  }

  @Test
  public void getModuleQuestionReturnsValidResponse() {
    questionRepositoryService.clearDatabase();
    questionRepositoryService.populateDatabase(Arrays.asList(question1, question2));
    question1.setModuleId("2");
    questionRepositoryService.populateDatabase(Arrays.asList(question1));
    assertEquals(2, questionRepositoryService.getModuleQuestions("1").size());
    assertEquals(1, questionRepositoryService.getModuleQuestions("2").size());
    assertEquals(0, questionRepositoryService.getModuleQuestions("3").size());
  }

  @Test
  public void duplicateInsertionThrowsException() {
    questionRepositoryService.clearDatabase();
    questionRepositoryService.populateDatabase(Arrays.asList(question1, question2));
    questionRepositoryService.populateDatabase(Arrays.asList(question1, question2));
    assertEquals(2, questionRepositoryService.getModuleQuestions("1").size());
  }
}
