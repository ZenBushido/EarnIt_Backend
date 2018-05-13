package com.mobiledi.earnitapi.web;

import org.joda.time.Instant;

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
    System.out.println(Instant.parse("2018-05-06T11:00:49.455Z"));

  }

}
