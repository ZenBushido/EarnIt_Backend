package com.mobiledi.earnitapi.util;

public final class MessageConstants {
	public static final int USER_EXISTS_CODE = 9000;
	public static final String USER_EXISTS = "A user with this email already exists";
	public static final int USER_DOES_NOT_EXISTS_CODE = 9001;
	public static final String USER_DOES_NOT_EXISTS = "A user with this email doesn't exists";

	public static final String PASSWORD_REMINDER_MAIL_SUBJECT = "Password Reminder";
	public static final String PASSWORD_REMINDER_MAIL_BODY = "Hello,\n\nYour Password is $PASSWORD.\n\nThanks,\nEarnIT Support.";
	public static final int MAIL_SENT_FAILED_CODE = 9002;
	public static final String MAIL_SENT_FAILED = "Failed to send mail.";
	public static final String MAIL_SENT_SUCCESS = "Mail sent.";

	public static final String MAIL_USERNAME = "support@myearnitapp.com";
	public static final String MAIL_PASSWORD = "C@pital1226";

	public static final String CHILDREN_DELETED = "Children Account Deleted.";
	public static final int CHILDREN_DELETED_FAILED_CODE = 9003;
    public static final String CHILDREN_DELETED_FAILED = "Invalid children id.";

	public static final String TASK_DELETED = "Task Deleted.";
	public static final int TASK_DELETED_FAILED_CODE = 9004;
	public static final String TASK_DELETED_FAILED = "Invalid task id.";

	public static final String GOAL_DELETED = "Goal Deleted.";
	public static final int GOAL_DELETED_FAILED_CODE = 9005;
	public static final String GOAL_DELETED_FAILED = "Invalid goal id.";

	public static final int GOAL_INVALID_ID_CODE = 9006;
	public static final String GOAL_INVALID_ID = "Invalid goal id.";

	public static final int GOAL_ID_NOT_MISSING_CODE = 9007;
	public static final String GOAL_ID_NOT_MISSING = "Goal id missing in request.";
}
