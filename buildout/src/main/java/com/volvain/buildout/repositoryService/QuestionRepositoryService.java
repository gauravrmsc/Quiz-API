package com.volvain.buildout.repositoryService;

import com.volvain.buildout.repositoryService.repository.QuestionRepository;
import com.volvain.buildout.repositoryService.repository.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class QuestionRepositoryService {

  @Autowired QuestionRepository questionRepository;

  public List<Question> getModuleQuestions(String moduleId) {
    List<Question> moduleQuestions = questionRepository.findAllByModuleId(moduleId);
    return moduleQuestions;
  }

  public int populateDatabase(List<Question> questions) {
    int count = 0;
    for (Question question : questions) {
      questionRepository.save(question);
      count++;
    }
    return count;
  }
}
