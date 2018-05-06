package com.mobiledi.earnitapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.math.BigDecimal;
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

	private BigDecimal amount;

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
	private BigDecimal tally;

	@Transient
	private BigDecimal tallyPercent;
	
	@Transient
	private BigDecimal cash;

	@OneToMany(mappedBy = "goal")
	private List<Adjustments> adjustments;

	private boolean isDeleted;

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
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

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
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

	public BigDecimal getTally() {
		return tally;
	}

	public void setTally(BigDecimal tally) {
		this.tally = tally;
	}

	public BigDecimal getTallyPercent() {
		return tallyPercent;
	}

	public void setTallyPercent(BigDecimal d) {
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