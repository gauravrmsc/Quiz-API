package com.volvain.buildout.repositoryservice.repository.entity;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@CompoundIndex(def = "{'questionId':1, 'moduleId':1}", name = "compound_index", unique = true)
public class Question {

  @Field("questionId")
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
