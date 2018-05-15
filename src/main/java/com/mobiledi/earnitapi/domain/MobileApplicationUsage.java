package com.mobiledi.earnitapi.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "mobile_application_usage")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class MobileApplicationUsage {

  @Id
  @SequenceGenerator(name = "MOBILE_APPLICATION_USAGE_ID_GENERATOR", sequenceName = "mobile_application_usage_id_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOBILE_APPLICATION_USAGE_ID_GENERATOR")
  private Long id;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @JoinColumn(name = "mobile_application_id")
  @ManyToOne(targetEntity = MobileApplication.class)
  private MobileApplication mobileApplication;

}