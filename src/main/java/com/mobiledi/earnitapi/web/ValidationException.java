package com.mobiledi.earnitapi.web;

import java.time.Instant;

//To encapsulate any type of validation error.
public class ValidationException extends RuntimeException {

  final private Integer errorCode;

  ValidationException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public static void main(String[] args) {
    System.out.println(Instant.now());
  }

}
