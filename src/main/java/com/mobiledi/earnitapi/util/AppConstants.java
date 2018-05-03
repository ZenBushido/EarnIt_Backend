package com.mobiledi.earnitapi.util;

public final class AppConstants {

	// USER TYPES
	public static String USER_PARENT = "PARENT";
	public static String USER_CHILD = "CHILD";

	// TASK_STATUS
	public static String TASK_CLOSED = "Closed";
	public static String TASK_CREATED = "Created";
	public static String TASK_REJECTED = "Rejected";
	public static String TASK_COMPLETED = "Completed";
	public static String TASK_REASSIGNED = "Reassigned";

	// AWS CREDS
	public static final String AWS_ACCESS_KEY_KEY = "AKIAISOCCTH4EXASGHTA";
	public static final String AWS_SECRET_ACCESS_KEY = "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";

	// TWILLIO CREDS
	// public static final String TWILIO_ACCOUNT_SID
	// ="AC64ba72b6d12c304233885bbbb8507f97";
	public static final String TWILIO_ACCOUNT_SID = "AC80499e4f3518b04fddca3f5cbe829743";
	// public static final String TWILIO_AUTH_TOKEN =
	// "3c1d4b02067f9be754f7b6c438e14e52";//
	public static final String TWILIO_AUTH_TOKEN = "bb12beb830a5b685b653b2ca00e74283";
	public static final String TWILIO_FROM = "+17207091360 ";

	// AWS CREDS
	public static final String ACCESS_KEY_KEY = "AKIAISOCCTH4EXASGHTA";
	public static final String SECRET_ACCESS_KEY = "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";

	// FCM CREDS
	public static final String FIREBASE_SERVER_KEY = "AAAAtDs9b0g:APA91bFtYyKFsmri0hP_XNJsA8vCYcaoK0KBDZdAWU_jpsooH089ZaAnYmFT6EcjOu0n_0Cqiakc4FfLM_JugolfhZF2X_EkhTCELgCH0wO5hNwv_BxDwOyJJDChx8k0iKavhsXpswQj";

	// PARENT - CHILD ACCOUNT ACTIONS
	public static enum ChildAccoutActionType {
		ADD, UPDATE;
	};

	// TASK REPEAT TYPE
	public static enum TaskFrequency {
		daily, weekly, monthly;
	};

	public static String GET_ALL_REPEAT_TASK_URL = "http://localhost:8080/earnit-api/tasks";
	public static String AUTH_EMAIL = "juliesawyer@gmail.com";
	public static String AUTH_PASSWORD = "test123";
	
	//APPSTORE URLS
	public static String ANDROID_PLAYSTORE = "https://goo.gl/nq4pzj";
	public static String IOS_APPSTORE = "https://goo.gl/V4FZAr";

	

}
