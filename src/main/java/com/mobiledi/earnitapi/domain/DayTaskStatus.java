package com.mobiledi.earnitapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "day_task_status")
public class DayTaskStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DAY_TASK_STATUS_ID_GENERATOR")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DAY_TASK_STATUS_ID_GENERATOR")
    private Integer id;

    @JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
    @Column(name = "create_date")
    private Timestamp createdDateTime;

    private String status;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private RepititionSchedule repititionSchedule;

    public DayTaskStatus() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public RepititionSchedule getRepititionSchedule() {
        return repititionSchedule;
    }

    public void setRepititionSchedule(RepititionSchedule repititionSchedule) {
        this.repititionSchedule = repititionSchedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
