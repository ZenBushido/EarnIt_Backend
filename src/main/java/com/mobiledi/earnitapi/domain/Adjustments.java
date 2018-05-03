package com.mobiledi.earnitapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Sunil Gulabani on 29-12-2017.
 */
@Entity
@Table(name = "goal_adjustments")
public class Adjustments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DAY_TASK_STATUS_ID_GENERATOR")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DAY_TASK_STATUS_ID_GENERATOR")
    private Integer id;

    private double amount;

    private String reason;

    @JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
    @Column(name = "create_date")
    private Timestamp createdDateTime;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Goal goal;

    public Adjustments() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}
