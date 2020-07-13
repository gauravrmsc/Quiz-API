package com.volvain.buildout.integ.test.configs;

public class AssessmentConfig {
  private String name;
  private String url;
  private String method;
  private String input;
  private int status;
  private String verification;

  public AssessmentConfig(String name, String url, String method, String input, int status,
      String verification, String response) {
    this.name = name;
    this.url = url;
    this.method = method;
    this.input = input;
    this.status = status;
    this.verification = verification;
    this.response = response;
  }

  public AssessmentConfig() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getVerification() {
    return verification;
  }

  public void setVerification(String verification) {
    this.verification = verification;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  private String response;
}
