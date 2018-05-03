package com.mobiledi.earnitapi.web;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.util.MessageConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.AppConstants.ChildAccoutActionType;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;
import com.mobiledi.earnitapi.util.SMSUtility;

import static com.mobiledi.earnitapi.util.MessageConstants.*;

@RestController
public class ChildrenController {
	private static Log logger = LogFactory.getLog(ChildrenController.class);

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

			logger.debug("Deleting child account: " + children.getId());
			childrenRepo.save(children);

			return new ResponseEntity<>(new Response(MessageConstants.CHILDREN_DELETED), HttpStatus.OK);
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, CHILDREN_DELETED_FAILED_CODE,
				CHILDREN_DELETED_FAILED);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/children", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Children child) throws JSONException {
		child.setUpdateDate(new Timestamp(new DateTime().getMillis()));
		
		Children lastChildRecord = childrenRepositoryCustom.findChild(child.getId());
		String lastUpdatedNumber = lastChildRecord.getPhone();
		Children childObject = childrenRepo.save(child);
		if(StringUtils.isNotBlank(child.getFcmToken()) && StringUtils.isNotBlank(child.getMessage()))
			PushNotifier.sendPushNotification(0, child.getFcmToken(), NotificationCategory.MESSAGE_TO_CHILD, child.getMessage());		
		
		if(lastChildRecord!= null){
		logger.info("previous phone number is : "+lastUpdatedNumber);
		logger.info("updating phone number to : "+child.getPhone());
			if(!lastUpdatedNumber.equalsIgnoreCase(child.getPhone()))
				logger.info("Message Sending success? :" + SMSUtility.SendSMS(child, ChildAccoutActionType.ADD));
		}
//		logger.info("Message Sending success? :" + SMSUtility.SendSMS(child, ChildAccoutActionType.ADD));
		if (childObject != null)
			return new ResponseEntity<Children>(child, HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

	}
}
