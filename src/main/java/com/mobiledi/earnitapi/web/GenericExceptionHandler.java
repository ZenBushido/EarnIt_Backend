package com.mobiledi.earnitapi.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//GenericExceptionHandler is responsible to handle the any kind of exception occurred while processing the request
@Slf4j
@RestControllerAdvice
public class GenericExceptionHandler {

  final private String GENERIC_MESSAGE = "An error occurred while processing the request.";

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> validationException(ValidationException ex) {
    log.error("Validation info : " + ex.getMessage());
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .errorCode(ex.getErrorCode().toString())
        .errorMessage(ex.getMessage()).build();

    return new ResponseEntity(errorResponseDto, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(IllegalAccessException.class)
  public ResponseEntity<Object> illegalAccessException(IllegalAccessException e) {
    log.error("IllegalAccessException info : " + e.getMessage());
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .errorCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
        .errorMessage(e.getMessage()).build();
    return new ResponseEntity(errorResponseDto, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> exception(Exception e) {
    log.error("Error occurred : ", e);
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + StringUtils.EMPTY)
        .errorMessage(GENERIC_MESSAGE).build();

    return new ResponseEntity(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

  }

}
