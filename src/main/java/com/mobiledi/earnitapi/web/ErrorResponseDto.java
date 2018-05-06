package com.mobiledi.earnitapi.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Data
@Builder
public class ErrorResponseDto {

  private final String errorMessage;
  private final String errorCode;

}
