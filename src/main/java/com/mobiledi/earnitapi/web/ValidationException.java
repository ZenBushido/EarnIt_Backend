package com.mobiledi.earnitapi.web;

import java.time.Instant;

//To encapsulate any type of validation error.
public class ValidationException extends RuntimeException {

  final private Integer httpStatus;

  ValidationException(String message, int httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public Integer getHttpStatus() {
    return httpStatus;
  }

  public static void main(String[] args) {
    System.out.println(Instant.now());
  }

}
