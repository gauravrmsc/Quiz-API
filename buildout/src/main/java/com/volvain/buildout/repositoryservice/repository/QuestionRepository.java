package com.volvain.buildout.repositoryservice.repository;

import com.volvain.buildout.repositoryservice.repository.entity.Question;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
  public List<Question> findAllByModuleId(String moduleName);
}
