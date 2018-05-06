package com.mobiledi.earnitapi.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//ExceptionHandler is responsible to handle the any kind of exception occurred while processing the request
@RestControllerAdvice
public class ExceptionHandler {

  final private String GENERIC_MESSAGE = "An error occurred while processing the request.";
  private static Log LOGGER = LogFactory.getLog(ExceptionHandler.class);


  @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> validationException(ValidationException ex) {

    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .errorCode(ex.getHttpStatus().toString())
        .errorMessage(ex.getMessage()).build();

    return new ResponseEntity(errorResponseDto, HttpStatus.valueOf(ex.getHttpStatus()));

  }

  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ResponseEntity<Object> exception(Exception e) {
    LOGGER.error(e);
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + StringUtils.EMPTY)
        .errorMessage(GENERIC_MESSAGE).build();

    return new ResponseEntity(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

  }

}
