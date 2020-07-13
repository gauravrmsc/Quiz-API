package com.volvain.buildout.repositoryservice;

import com.volvain.buildout.repositoryservice.repository.QuestionRepository;
import com.volvain.buildout.repositoryservice.repository.entity.Question;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionRepositoryService {

  @Autowired
  QuestionRepository questionRepository;

  public List<Question> getModuleQuestions(String moduleId) {
    List<Question> moduleQuestions = questionRepository.findAllByModuleId(moduleId);
    return moduleQuestions;
  }

  public int populateDatabase(List<Question> questions) {
    int count = 0;
    for (Question question : questions) {
      try {
        questionRepository.save(question);
        count++;
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    return count;
  }

  public void clearDatabase() {
    questionRepository.deleteAll();
  }
}
