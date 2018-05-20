package com.mobiledi.earnitapi.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@EqualsAndHashCode
public class IgnoreMobileAppDto {

  private final String appName;
  private final Integer childrenId;
}
