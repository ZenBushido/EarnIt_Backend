package com.mobiledi.earnitapi.util;

public final class NotificationConstants {

	public static enum NotificationCategory {
		TASK_CREATED, TASK_UPDATED, TASK_COMPLETED, TASK_REASSIGNED, TASK_CLOSED, TASK_REJECTED, TASK_TO_OVERDUE, GOAL_REACHED, MESSAGE_TO_CHILD;
	}

	public static String TASK_CREATED_TITLE = "New task added";
	public static String TASK_UPDATED_TITLE = "Task was updated";
	public static String TASK_CLOSED_TITLE = "Task approved";
	public static String TASK_COMPLETED_TITLE = "Task Completed";
	public static String TASK_REJECTED_TITLE = "Task Rejected";
	public static String TASK_REASSIGNED_TITLE = "Task assigned to you";
	public static String TASK_TO_OVERDUE_TITLE = "Task overdues soon";
	public static String GOAL_GOAL_REACHED = "Goal completed";
	public static String MESSAGE_TO_CHILD_TITLE = "New message from parent";

}
