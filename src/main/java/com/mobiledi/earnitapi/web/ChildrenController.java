package com.mobiledi.earnitapi.web;

import static com.mobiledi.earnitapi.util.MessageConstants.CHILDREN_DELETED_FAILED;
import static com.mobiledi.earnitapi.util.MessageConstants.CHILDREN_DELETED_FAILED_CODE;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.MessageConstants;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChildrenController {

	@Autowired
	private ChildrenRepository childrenRepo;
	
	@Autowired
	private ChildrenRepositoryCustom childrenRepositoryCustom;

	@RequestMapping("/childrens/{id}")
	public List<Children> findById(@PathVariable int id) {

		List<Children> childrens = childrenRepo.findChildrenByAccountIdAndIsDeletedOrderByFirstNameAsc(id, false);
		childrens.forEach(child -> {

			List<Task> toRemove = new ArrayList<>();
			child.getTasks().forEach(task -> {
				if (task.getStatus().equals(AppConstants.TASK_CLOSED)) {
					toRemove.add(task);
				}
			});
			child.getTasks().removeAll(toRemove);
			child.setUserType(AppConstants.USER_CHILD);
		});
		return childrens;
	}

	@RequestMapping(value = "/childrens/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteChild(@PathVariable Integer id) {
		Optional<Children> childrenOptional = childrenRepo.findById(id);
		if(childrenOptional.isPresent()) {
			Children children = childrenOptional.get();
			children.setDeleted(true);
			children.setUpdateDate(new Timestamp(new DateTime().getMillis()));

			log.debug("Deleting child account: " + children.getId());
			childrenRepo.save(children);

			return new ResponseEntity<>(new Response(MessageConstants.CHILDREN_DELETED), HttpStatus.OK);
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, CHILDREN_DELETED_FAILED_CODE,
				CHILDREN_DELETED_FAILED);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/children", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Children child) throws JSONException {

		doesChildExist(child.getId());
		child = childrenRepositoryCustom.updateChild(child);
		if (StringUtils.isNotBlank(child.getFcmToken()) && StringUtils.isNotBlank(child.getMessage())) {
			PushNotifier
					.sendPushNotification(0, child.getFcmToken(), NotificationCategory.MESSAGE_TO_CHILD,
							child.getMessage());
		}

		return new ResponseEntity<Children>(child, HttpStatus.ACCEPTED);

	}

	private void doesChildExist(Integer id) {
		Optional<Children> children = childrenRepo.findById(id);
		if (!children.isPresent()) {
			throw new ValidationException("Children not found with id : " + id, 400);
		}
	}

}
