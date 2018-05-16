package com.mobiledi.earnitapi.dto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class MobileApplicationUsageDto {

  private final String startDateTimeWithZone;
  private final String endDateTimeWithZone;

}
