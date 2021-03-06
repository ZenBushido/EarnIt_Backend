package com.mobiledi.earnitapi.web;

import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_DELETED;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_DELETED_FAILED;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_DELETED_FAILED_CODE;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_ID_NOT_MISSING;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_ID_NOT_MISSING_CODE;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_INVALID_ID;
import static com.mobiledi.earnitapi.util.MessageConstants.GOAL_INVALID_ID_CODE;

import com.mobiledi.earnitapi.domain.Adjustments;
import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.AdjustmentsRepository;
import com.mobiledi.earnitapi.repository.GoalRepository;
import com.mobiledi.earnitapi.repository.TasksRepository;
import com.mobiledi.earnitapi.util.AppConstants;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GoalController {

	@Autowired
	GoalRepository goalRepo;
	
	@Autowired
	TasksRepository taskRepo;

	@Autowired
	private AdjustmentsRepository adjustmentsRepository;

	@RequestMapping(value = "/goals", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Goal goal) throws JSONException {

		goal.setCreateDate(new Timestamp(new DateTime().getMillis()));
		goal.setDeleted(false);
		Goal goalObject = goalRepo.save(goal);
		if (goalObject != null) {
			return new ResponseEntity<>(goalObject, HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@RequestMapping(value = "/adjustments", method = RequestMethod.POST)
	private ResponseEntity<?> saveAdjustments(@RequestBody Adjustments adjustments) {
		ApiError apiError = null;

		if(adjustments.getGoal() ==null || adjustments.getGoal().getId() == null) {
			apiError = new ApiError(HttpStatus.BAD_REQUEST, GOAL_ID_NOT_MISSING_CODE, GOAL_ID_NOT_MISSING);
		} else {
			Optional<Goal> goalOptional = goalRepo.findById(adjustments.getGoal().getId());

			if(goalOptional.isPresent()) {
				adjustments.setCreatedDateTime(new Timestamp(new DateTime().getMillis()));
				adjustmentsRepository.save(adjustments);

				Goal goal = goalOptional.get();
				goal.setUpdateDate(adjustments.getCreatedDateTime());
				goal = goalRepo.save(goal);

				return new ResponseEntity<>(goal, HttpStatus.OK);
			} else {
				apiError = new ApiError(HttpStatus.BAD_REQUEST, GOAL_INVALID_ID_CODE, GOAL_INVALID_ID);
			}
		}
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/goals", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Goal goal) throws JSONException {

		goal.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		Goal goalObject = goalRepo.save(goal);
		if (goalObject != null)
			return new ResponseEntity<>(goalObject, HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@RequestMapping("/goals/{id}")
	public List<Goal> findByChildrenId(@PathVariable int id) {
		List<Goal> goals = goalRepo.findByChildrenIdAndIsDeleted(id, false);

		if(CollectionUtils.isEmpty(goals))
			return new ArrayList<>();

		for (Goal goal : goals) {
			BigDecimal tally = new BigDecimal(0.0);
			for (Task task : goal.getTasks()) {
				if (task.getStatus().equalsIgnoreCase(AppConstants.TASK_CLOSED)) {
					tally = tally.add(task.getAllowance());
				}
			}
			goal.setTally(tally);
			goal.setTallyPercent(tally
					.divide(goal.getAmount().multiply(BigDecimal.valueOf(100)), 2, BigDecimal.ROUND_HALF_UP));
		}
		
		List<Task> tasks = taskRepo.findByChildrenIdAndIsDeleted(id, false);
		BigDecimal cash = new BigDecimal(0);
		for(Task task : tasks){
			if (task.getStatus().equalsIgnoreCase(AppConstants.TASK_CLOSED) && task.getGoal() == null) {
				cash = cash.add(task.getAllowance());
			}
		}

		goals.get(0).setCash(cash);
		return goals;
	}

	@RequestMapping(value = "/goals/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Integer id) throws JSONException {
		Optional<Goal> goalOptional = goalRepo.findById(id);
		if(goalOptional.isPresent()) {
			Goal goal = goalOptional.get();
			goal.setDeleted(true);
			goal.setUpdateDate(new Timestamp(new DateTime().getMillis()));

			log.debug("Deleting goal: " + goal.getId());
			goalRepo.save(goal);

			if(goal.getTasks() != null) {
				goal.getTasks().forEach(task -> {
					task.setDeleted(true);
					task.setUpdateDate(new Timestamp(new DateTime().getMillis()));

					log.debug("Deleting task: " + task.getId());
					taskRepo.save(task);
				});
			}

			return new ResponseEntity<>(new Response(GOAL_DELETED), HttpStatus.OK);
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, GOAL_DELETED_FAILED_CODE,
				GOAL_DELETED_FAILED);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
}
