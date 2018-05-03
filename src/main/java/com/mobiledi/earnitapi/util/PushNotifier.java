package com.mobiledi.earnitapi.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;

import us.raudi.pushraven.Pushraven;

final public class PushNotifier {
	private static Log logger = LogFactory.getLog(PushNotifier.class);

	public static boolean sendPushNotification(int childId, String deviceKey, NotificationCategory type, String messageBody) {

		if(StringUtils.isEmpty(deviceKey)) {
			logger.error("No device key found for sending push notification: " + type + ", messageBody: " + messageBody);
			return false;
		}

		Pushraven.notification.clear(); // clears the notification, equatable
										// with "raven = new Notification();"
		Pushraven.notification.clearAttributes(); // clears FCM protocol
													// paramters excluding
													// targets
		Pushraven.notification.clearTargets();

		Pushraven.setKey(AppConstants.FIREBASE_SERVER_KEY);

		logger.info("CREATING PUSH FOR: " + deviceKey + " " + type);
		switch (type) {
			case TASK_REASSIGNED:
				Pushraven.notification.title(NotificationConstants.TASK_REASSIGNED_TITLE).color("#ff0000");
				break;
		case TASK_CREATED:
			Pushraven.notification.title(NotificationConstants.TASK_CREATED_TITLE).color("#ff0000");
			break;
		case TASK_UPDATED:
			Pushraven.notification.title(NotificationConstants.TASK_UPDATED_TITLE).color("#ff0000");
			break;
		case TASK_CLOSED:
			Pushraven.notification.title(NotificationConstants.TASK_CLOSED_TITLE).color("#ff0000");
			break;
		case TASK_COMPLETED:
			Pushraven.notification.title(NotificationConstants.TASK_COMPLETED_TITLE).color("#ff0000");
			break;
		case TASK_REJECTED:
			Pushraven.notification.title(NotificationConstants.TASK_REJECTED_TITLE).color("#ff0000");
			break;
		case TASK_TO_OVERDUE:
			Pushraven.notification.title(NotificationConstants.TASK_TO_OVERDUE_TITLE).color("#ff0000");
			break;
		case GOAL_REACHED:
			Pushraven.notification.title(NotificationConstants.GOAL_GOAL_REACHED).color("#ff0000");

		case MESSAGE_TO_CHILD:
			Pushraven.notification.title(NotificationConstants.MESSAGE_TO_CHILD_TITLE).color("#ff0000");
			break;

		}

		Pushraven.notification.body_loc_key(String.valueOf(childId));
		Pushraven.notification.to(deviceKey).text(messageBody).tag(type.name());
		Pushraven.push();

		return true;
	}
}
