package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.services.GoalService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.DayTaskStatusRepository;
import com.mobiledi.earnitapi.repository.GoalRepository;
import com.mobiledi.earnitapi.util.MessageConstants;
import com.mobiledi.earnitapi.util.NotificationConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.domain.TaskComment;
import com.mobiledi.earnitapi.repository.TaskCommentRepository;
import com.mobiledi.earnitapi.repository.TasksRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.repository.custom.TaskRepositoryCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;

import static com.mobiledi.earnitapi.util.MessageConstants.*;

@RestController
public class TaskController {
	private static Log logger = LogFactory.getLog(TaskController.class);

	@Autowired
	TasksRepository taskRepo;

	@Autowired
	TaskRepositoryCustom taskRepoCustom;

	@Autowired
	TaskCommentRepository taskCommentRepo;

	@Autowired
	ChildrenRepositoryCustom childRepo;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private DayTaskStatusRepository dayTaskStatusRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private GoalService goalService;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public List<Task> findAllRepeatTAsk() {
		logger.info("fetchign repeat task list ");

		List<Task> tasks = taskRepoCustom.fetchRepeatTask();
		return tasks;

	}

	@RequestMapping(value = "/tasks/{childId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByChildId(@PathVariable int childId) throws JSONException {
		return new ResponseEntity<Iterable<Task>>(taskRepo.findByChildrenIdAndIsDeleted(childId, false), HttpStatus.OK);
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Task task) throws JSONException {

		task.setCreateDate(new Timestamp(new DateTime().getMillis()));
		task.setStatus(AppConstants.TASK_CREATED);

		Task taskObject = taskRepo.save(task);
		sendPushNotification(taskObject);

		return new ResponseEntity<>(taskObject, HttpStatus.CREATED);
	}

	private void sendPushNotification(Task taskObject) {
		if(taskObject == null)
			return;

		Children notifyChild = childRepo.findChild(taskObject.getChildren().getId());
		logger.info("Children id/email/FCM ID on ADD task" + notifyChild.getId() + " /" + notifyChild.getEmail() + " /"
				+ notifyChild.getFcmToken());

		if (StringUtils.isNotBlank(notifyChild.getFcmToken())) {
				PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_CREATED,
						taskObject.getName());
		}
	}

	@RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Integer id) throws JSONException {
		Optional<Task> taskOptional = taskRepo.findById(id);
		if(taskOptional.isPresent()) {
			Task task = taskOptional.get();
			task.setDeleted(true);
			task.setUpdateDate(new Timestamp(new DateTime().getMillis()));

			logger.debug("Deleting task: " + task.getId());
			taskRepo.save(task);

			return new ResponseEntity<>(new Response(TASK_DELETED), HttpStatus.OK);
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, TASK_DELETED_FAILED_CODE,
				TASK_DELETED_FAILED);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Task task) throws JSONException {

		Optional<Task> taskOptional = taskRepo.findById(task.getId());

		validateTask(taskOptional, task.getId());

		final Task persistedTask = taskRepo.findById(task.getId()).get();

		if (task.getTaskComments() != null && task.getTaskComments().size() > 0) {

			// Save any Task comment
			// List<TaskComment> taskComments= new ArrayList<TaskComment>();
			task.getTaskComments().forEach(taskComment -> {
				TaskComment toSave = taskComment;
				toSave.setCreateDate(new Timestamp(new DateTime().getMillis()));
				toSave.setTask(persistedTask);
				taskCommentRepo.save(toSave);
			});

			// task.setTaskComments(taskComments);

		}

		if(task.getRepititionSchedule() != null && task.getRepititionSchedule().getDayTaskStatuses() != null) {
			task.getRepititionSchedule().getDayTaskStatuses().forEach(dayTaskStatus -> {
				dayTaskStatusRepository.save(dayTaskStatus);
			});
		}

		persistedTask.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		Task taskObject = taskRepo.save(persistedTask);
		Children notifyChild = taskObject.getChildren();
		logger.info(" Children fcm ID" + notifyChild.getFcmToken());

		if (taskObject != null) {
			if (StringUtils.isNotBlank(notifyChild.getFcmToken())) {

				if (taskObject.getStatus().equals(AppConstants.TASK_CLOSED)) {
					PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_CLOSED,
							task.getName());
					// CHECK IF THE GOAL HAS BEEN COMPLETED
					boolean isGoalReached = goalService.checkIfGoalReached(taskObject.getGoal());
					if (isGoalReached) {
						PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(),
								NotificationCategory.GOAL_REACHED, task.getName());
						for (Parent parent : taskObject.getChildren().getAccount().getParents()) {
							if (StringUtils.isNotBlank(parent.getFcmToken()))
								PushNotifier.sendPushNotification(0, parent.getFcmToken(),
										NotificationCategory.GOAL_REACHED, task.getName());
						}
					}
				} else if (taskObject.getStatus().equals(AppConstants.TASK_REJECTED)) {
					taskObject.setStatus(AppConstants.TASK_CREATED);
					taskObject = taskRepo.save(taskObject);
					PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_REJECTED,
							task.getName());
				} else if (taskObject.getStatus().equals(AppConstants.TASK_COMPLETED)) {
					for (Parent parent : taskObject.getChildren().getAccount().getParents()) {
						if (StringUtils.isNotBlank(parent.getFcmToken()))
							PushNotifier.sendPushNotification(notifyChild.getId(), parent.getFcmToken(),
									NotificationCategory.TASK_COMPLETED, task.getName());
					}
				} else if (taskObject.getStatus().equals(AppConstants.TASK_REASSIGNED)) {
					taskReassigned(taskObject, notifyChild);
				} else {
					PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_UPDATED,
							task.getName());
				}

			} else if (taskObject.getStatus().equals(AppConstants.TASK_REASSIGNED)) {
				taskReassigned(taskObject, notifyChild);
			}

		}

		return new ResponseEntity<Task>(taskObject, HttpStatus.ACCEPTED);

	}

	private void taskReassigned(Task taskObject, Children notifyChild) {
		taskObject.getGoal().setChildren(notifyChild);

		Goal goal = goalRepository.save(taskObject.getGoal());

		goal.getTasks().forEach(task1 -> {
            task1.setChildren(notifyChild);
            taskRepo.save(task1);
            PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_REASSIGNED,
                    task1.getName());
        });
	}

	private void validateTask(Optional taskOptional, Integer taskId) {
		if (!taskOptional.isPresent()) {
			throw new ValidationException("Task with id : " + taskId + " does not exist.", 400);
		}
	}
}
