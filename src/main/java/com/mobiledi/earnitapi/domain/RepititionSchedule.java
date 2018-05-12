package com.mobiledi.earnitapi.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.CollectionUtils;

/**
 * The persistent class for the repitition_schedule database table.
 *
 */
@Entity
@Table(name = "repitition_schedule")
@NamedQuery(name = "RepititionSchedule.findAll", query = "SELECT r FROM RepititionSchedule r")
@JsonIgnoreProperties({ "tasks" })
public class RepititionSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "REPITITION_SCHEDULE_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPITITION_SCHEDULE_ID_GENERATOR")
	private Integer id;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "end_time")
	private String endTime;

	private String repeat;

	@Column(name = "every_n_repeat")
	private Integer everyNRepeat;

	@Column(name = "perform_task_on_the_n_day")
	private String performTaskOnTheNSpecifiedDay;

	@ElementCollection
	private Set<String> specificDays;// Monday, Tuesday, Wednesday Thursday, Friday, Saturday, Sunday

	@OneToMany(mappedBy = "repititionSchedule")
	private List<DayTaskStatus> dayTaskStatuses;

	// bi-directional many-to-one association to Task
	@OneToMany(mappedBy = "repititionSchedule")
	private List<Task> tasks;

	public RepititionSchedule() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<DayTaskStatus> getDayTaskStatuses() {
		return dayTaskStatuses;
	}

	public void setDayTaskStatuses(List<DayTaskStatus> dayTaskStatuses) {
		this.dayTaskStatuses = dayTaskStatuses;
	}

	public Set<String> getSpecificDays() {
		if(!CollectionUtils.isEmpty(specificDays)) {
			return specificDays.parallelStream().map(String::toLowerCase).collect(Collectors.toSet());
		}
		return specificDays;
	}

	public void setSpecificDays(Set<String> specificDays) {
		this.specificDays = specificDays;
	}

	public Integer getEveryNRepeat() {
		return everyNRepeat;
	}

	public void setEveryNRepeat(Integer everyNRepeat) {
		this.everyNRepeat = everyNRepeat;
	}

	public String getPerformTaskOnTheNSpecifiedDay() {
		return performTaskOnTheNSpecifiedDay;
	}

	public void setPerformTaskOnTheNSpecifiedDay(String performTaskOnTheNSpecifiedDay) {
		this.performTaskOnTheNSpecifiedDay = performTaskOnTheNSpecifiedDay;
	}

}
