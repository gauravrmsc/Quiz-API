package com.volvain.buildout.repositoryService.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;


@Data

public class Question {
  @Id
  String questionId;
  @Field("moduleId")
  String moduleId = "1";
  @Field("title")
  String title;
  @Field("description")
  String description;
  @Field("type")
  String type;
  @Field("options")
  Map<String, String> options;
  @Field("correctAnswer")
  List<String> correctAnswer;
  @Field("explanation")
  String explanation;

}
