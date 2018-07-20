package com.mobiledi.earnitapi.web;

import static com.mobiledi.earnitapi.util.MessageConstants.TASK_DELETED;
import static com.mobiledi.earnitapi.util.MessageConstants.TASK_DELETED_FAILED;
import static com.mobiledi.earnitapi.util.MessageConstants.TASK_DELETED_FAILED_CODE;

import com.mobiledi.earnitapi.constants.StringConstant;
import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.domain.TaskComment;
import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.DayTaskStatusRepository;
import com.mobiledi.earnitapi.repository.GoalRepository;
import com.mobiledi.earnitapi.repository.TaskCommentRepository;
import com.mobiledi.earnitapi.repository.TasksRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.repository.custom.TaskRepositoryCustom;
import com.mobiledi.earnitapi.services.FileStorageService;
import com.mobiledi.earnitapi.services.GoalServiceCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.ImageUtil;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class TaskController {

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
	private GoalServiceCustom goalServiceCustom;

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private FileStorageService fileStorageService;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public List<Task> findAllRepeatTAsk() {
		log.info("fetching repeat task list ");

		List<Task> tasks = taskRepoCustom.fetchRepeatTask();
		return tasks;

	}

	@RequestMapping(value = "/tasks/{childId}", method = RequestMethod.GET)
	public ResponseEntity<?> findByChildId(@PathVariable int childId) throws JSONException {
		return new ResponseEntity<Iterable<Task>>(taskRepo.findByChildrenIdAndIsDeleted(childId, false), HttpStatus.OK);
	}

	@RequestMapping(value = "/tasks/{taskId}/images", method = RequestMethod.POST)
	public String update(@PathVariable int taskId, @RequestParam("file") MultipartFile file) {
		Optional<Task> task = taskRepo.findById(taskId);
		if(!task.isPresent()){
			throw new ValidationException("Task not found with task id : " + taskId, HttpStatus.NOT_FOUND.value());
		}
		File temporaryFile = imageUtil.getTemporaryFileFromMultipartFile(file);
		String taskImageUrl = imageUtil.createTaskImageUrl(task.get(), file);
		fileStorageService.storeFile(taskImageUrl, temporaryFile);
		temporaryFile.delete();
		return taskImageUrl;
	}

	@GetMapping(value = "/tasks/{taskId}/images/{imageName}")
	@SneakyThrows
	public void getTaskPicture(@PathVariable Integer taskId, @PathVariable String imageName,
			HttpServletResponse httpServletResponse) {
		Optional<Task> task = taskRepo.findById(taskId);
		if (!task.isPresent()) {
			throw new ValidationException("Task not found with task id : " + taskId,
					HttpStatus.NOT_FOUND.value());
		}
		InputStream inputStream = null;
		try {
			String taskImageUrl = imageUtil.createTaskImageUrl(task.get(), imageName);
			inputStream = fileStorageService.getFile(taskImageUrl);
			httpServletResponse.setContentType(StringConstant.CONTENT_TYPE_OCTET_STREAM);
			IOUtils.copyLarge(inputStream, httpServletResponse.getOutputStream());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Task task) throws JSONException {

		task.setCreateDate(new Timestamp(new DateTime().getMillis()));
		task.setStatus(AppConstants.TASK_CREATED);
		task.setDeleted(false);
		Task taskObject = taskRepo.save(task);
		sendPushNotification(taskObject);

		return new ResponseEntity<>(taskObject, HttpStatus.CREATED);
	}

	private void sendPushNotification(Task taskObject) {
		if(taskObject == null)
			return;

		Children notifyChild = childRepo.findChild(taskObject.getChildren().getId());
		log.info("Children id/email/FCM ID on ADD task" + notifyChild.getId() + " /" + notifyChild.getEmail() + " /"
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

			log.debug("Deleting task: " + task.getId());
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
				dayTaskStatus.setRepititionSchedule(persistedTask.getRepititionSchedule());
				dayTaskStatusRepository.save(dayTaskStatus);
			});
		}

		persistedTask.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		if (Objects.nonNull(task.getAllowance())) {
			persistedTask.setAllowance(task.getAllowance());
		}

		if (Objects.nonNull(task.getName())) {
			persistedTask.setName(task.getName());
		}

		if (Objects.nonNull(task.getStatus())) {
			persistedTask.setStatus(task.getStatus());
		}

		if (Objects.nonNull(task.getDueDate())) {
			persistedTask.setDueDate(task.getDueDate());
		}

		Task taskObject = taskRepo.save(persistedTask);
		Children notifyChild = taskObject.getChildren();
		log.info(" Children fcm ID" + notifyChild.getFcmToken());

		if (taskObject != null) {
			if (StringUtils.isNotBlank(notifyChild.getFcmToken())) {

				if (taskObject.getStatus().equals(AppConstants.TASK_CLOSED)) {
					PushNotifier.sendPushNotification(0, notifyChild.getFcmToken(), NotificationCategory.TASK_CLOSED,
							task.getName());
					// CHECK IF THE GOAL HAS BEEN COMPLETED
					boolean isGoalReached = goalServiceCustom.checkIfGoalReached(taskObject.getGoal());
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
