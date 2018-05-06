package com.mobiledi.earnitapi.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * The persistent class for the tasks database table.
 * 
 */
@Entity
@Table(name = "tasks")
@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TASKS_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASKS_ID_GENERATOR")
	private Integer id;

	private BigDecimal allowance;

	@Column(name = "create_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp createDate;

	@Column(name = "due_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp dueDate;

	private String name;

	@Column(name = "picture_required")
	private Integer pictureRequired;

	private String status;

	@Column(name = "update_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp updateDate;

	// bi-directional many-to-one association to TaskComment
	@OneToMany(mappedBy = "task")
	private List<TaskComment> taskComments;

	// bi-directional many-to-one association to Children
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "child_id")
	private Children children;

	// bi-directional many-to-one association to Goal
	@ManyToOne
	// @JsonProperty(access = Access.WRITE_ONLY)
	private Goal goal;

	// bi-directional many-to-one association to RepititionSchedule
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "repetition_schedule_id")
	private RepititionSchedule repititionSchedule;

	private String description;

	private boolean isDeleted;

	@Column(name = "should_lock_apps_if_task_overdue")
	private boolean shouldLockAppsIfTaskOverdue;

	public Task() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAllowance() {
		return this.allowance;
	}

	public void setAllowance(BigDecimal allowance) {
		this.allowance = allowance;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPictureRequired() {
		return this.pictureRequired;
	}

	public void setPictureRequired(Integer pictureRequired) {
		this.pictureRequired = pictureRequired;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public List<TaskComment> getTaskComments() {
		return this.taskComments;
	}

	public void setTaskComments(List<TaskComment> taskComments) {
		this.taskComments = taskComments;
	}

	public TaskComment addTaskComment(TaskComment taskComment) {
		getTaskComments().add(taskComment);
		taskComment.setTask(this);

		return taskComment;
	}

	public TaskComment removeTaskComment(TaskComment taskComment) {
		getTaskComments().remove(taskComment);
		taskComment.setTask(null);

		return taskComment;
	}

	public Children getChildren() {
		return this.children;
	}

	public void setChildren(Children children) {
		this.children = children;
	}

	public Goal getGoal() {
		return this.goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public RepititionSchedule getRepititionSchedule() {
		return this.repititionSchedule;
	}

	public void setRepititionSchedule(RepititionSchedule repititionSchedule) {
		this.repititionSchedule = repititionSchedule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public boolean isShouldLockAppsIfTaskOverdue() {
		return shouldLockAppsIfTaskOverdue;
	}

	public void setShouldLockAppsIfTaskOverdue(boolean shouldLockAppsIfTaskOverdue) {
		this.shouldLockAppsIfTaskOverdue = shouldLockAppsIfTaskOverdue;
	}
}