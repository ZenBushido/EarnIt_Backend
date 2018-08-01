package com.mobiledi.earnitapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "mobile_application")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class MobileApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @Column(name = "ignored_by_parent")
  private Boolean ignoredByParent = false;

  @JsonProperty(access = Access.WRITE_ONLY)
  @JoinColumn(name = "children_id")
  @ManyToOne(targetEntity = Children.class)
  private Children children;

  @JsonIgnore
  @OneToMany(mappedBy = "mobileApplication", cascade = CascadeType.ALL, targetEntity = MobileApplicationUsage.class)
  private List<MobileApplicationUsage> mobileApplicationUsages = new ArrayList<>();

}