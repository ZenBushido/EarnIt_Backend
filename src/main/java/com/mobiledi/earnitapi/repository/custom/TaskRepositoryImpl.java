package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.domain.QTask;
import com.mobiledi.earnitapi.domain.Task;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public List<Task> fetchRepeatTask() {
		QTask task = QTask.task;
		JPAQuery<Task> query = new JPAQuery<Task>(entityManager);
		List<Task> tasks = query.select(task).from(task).where(task.repititionSchedule.isNotNull())
				.fetch();
		log.info("Total count for reapet tasks found : " + tasks.size());

		return tasks.parallelStream().filter(task1 -> !task1.isDeleted()).collect(Collectors.toList());
		/*List<Task> returnList = new ArrayList<Task>();
		for (Task singleTask : tasks) {

			if(!singleTask.isDeleted()) {
				DateTime currentDate = new DateTime().withTimeAtStartOfDay();
				DateTime taskCreateDate = new DateTime(singleTask.getCreateDate());
				String taskFrequency = singleTask.getRepititionSchedule().getRepeat();
				if (taskFrequency.equalsIgnoreCase(AppConstants.TaskFrequency.daily.toString())) {
					if (currentDate.equals(taskCreateDate.plusDays(1).withTimeAtStartOfDay()) ||
							(Days.daysBetween(taskCreateDate, currentDate).getDays() > 0)) {
						returnList.add(autoCreateTask(taskFrequency, singleTask));
					}
				} else if (taskFrequency.equalsIgnoreCase(AppConstants.TaskFrequency.weekly.toString())) {
					if (currentDate.equals(taskCreateDate.plusWeeks(1).withTimeAtStartOfDay())
							|| (Days.daysBetween(taskCreateDate, currentDate).getDays() > 0 ?
							((Days.daysBetween(taskCreateDate, currentDate).getDays() % 6 == 0 ? true : false)) : false)) {
						returnList.add(autoCreateTask(taskFrequency, singleTask));
					}
				} else if (taskFrequency.equalsIgnoreCase(AppConstants.TaskFrequency.monthly.toString())) {
					if (currentDate.equals(taskCreateDate.plusMonths(1).withTimeAtStartOfDay())) {
						returnList.add(autoCreateTask(taskFrequency, singleTask));
					}
				}
			}
		}
		logger.info("auto generated reapet tasks count : " + returnList.size());
		return returnList;*/
	}

	/*public Task autoCreateTask(String value, Task task) {
		TaskFrequency frequency = TaskFrequency.valueOf(value);
		Task returntask = new Task();
		long setExpiryDate;
		switch (frequency) {
		case daily:
			setExpiryDate = new DateTime().plusDays(1).getMillis();
			returntask = addRepeatTask(setExpiryDate, task);
			break;

		case weekly:
			setExpiryDate = new DateTime().plusWeeks(1).getMillis();
			returntask = addRepeatTask(setExpiryDate, task);
			break;

		case monthly:
			setExpiryDate = new DateTime().plusMonths(1).getMillis();
			returntask = addRepeatTask(setExpiryDate, task);
			break;

		}
		return returntask;
	}

	public Task addRepeatTask(long expiryTime, Task oldTask) {
		int numberDays = Days.daysBetween(new DateTime(oldTask.getCreateDate()), new DateTime(oldTask.getDueDate()))
				.getDays();
		Task newRepeatTask = new Task();
		newRepeatTask.setAllowance(oldTask.getAllowance());
		newRepeatTask.setCreateDate(new Timestamp(new DateTime().getMillis()));
		newRepeatTask.setUpdateDate(new Timestamp(new DateTime().getMillis()));
		newRepeatTask.setName(oldTask.getName());
		newRepeatTask.setPictureRequired(oldTask.getPictureRequired());
		newRepeatTask.setStatus(AppConstants.TASK_CREATED);
		newRepeatTask.setDueDate(new Timestamp(
				numberDays > 0 ? new DateTime().plusDays(numberDays).getMillis() : new DateTime().getMillis()));
		newRepeatTask.setDescription(oldTask.getDescription());
		newRepeatTask.setGoal(oldTask.getGoal());
		newRepeatTask.setRepititionSchedule(oldTask.getRepititionSchedule());
		newRepeatTask.getRepititionSchedule().setExpiryDateTime(new Timestamp(expiryTime));
		newRepeatTask.setChildren(oldTask.getChildren());
		entityManager.persist(newRepeatTask);
		oldTask.setRepititionSchedule(null);
		entityManager.merge(oldTask);
		return newRepeatTask;
	}*/

}
