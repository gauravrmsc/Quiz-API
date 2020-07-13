package com.volvain.buildout.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorHandler implements ErrorController {

  @RequestMapping("/error")
  public ResponseEntity<Object> sendErrorMessage() {
    return new ResponseEntity<Object>("Bad Request", HttpStatus.BAD_REQUEST);
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
