package com.mobiledi.earnitapi.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * The persistent class for the goals database table.
 * 
 */
@Entity
@Table(name = "goals")
@NamedQuery(name = "Goal.findAll", query = "SELECT g FROM Goal g")
@JsonIgnoreProperties({ "tasks" })
public class Goal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "GOALS_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GOALS_ID_GENERATOR")
	private Integer id;

	private double amount;

	@Column(name = "create_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp createDate;

	private String name;

	@Column(name = "update_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp updateDate;

	// bi-directional many-to-one association to Children
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Children children;

	// bi-directional many-to-one association to Task

	@OneToMany(mappedBy = "goal")
	private List<Task> tasks;

	@Transient
	private double tally;

	@Transient
	private double tallyPercent;
	
	@Transient
	private double cash;

	@OneToMany(mappedBy = "goal")
	private List<Adjustments> adjustments;

	private boolean isDeleted;

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public Goal() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Children getChildren() {
		return this.children;
	}

	public void setChildren(Children children) {
		this.children = children;
	}

	public double getTally() {
		return tally;
	}

	public void setTally(double tally) {
		this.tally = tally;
	}

	public double getTallyPercent() {
		return tallyPercent;
	}

	public void setTallyPercent(double d) {
		this.tallyPercent = d;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public List<Adjustments> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<Adjustments> adjustments) {
		this.adjustments = adjustments;
	}
}