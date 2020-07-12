package com.volvain.buildout.repositoryService.repository;

import com.volvain.buildout.repositoryService.repository.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    public List<Question> findAllByModuleId(String moduleName);
}
