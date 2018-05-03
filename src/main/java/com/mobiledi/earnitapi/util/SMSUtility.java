package com.mobiledi.earnitapi.util;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.util.AppConstants.ChildAccoutActionType;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSUtility {

	public static boolean SendSMS(Children child, ChildAccoutActionType action) {
		try {
			Twilio.init(AppConstants.TWILIO_ACCOUNT_SID, AppConstants.TWILIO_AUTH_TOKEN);
			Message message = Message.creator(new PhoneNumber(child.getPhone()),
					new PhoneNumber(AppConstants.TWILIO_FROM), SMSUtility.messageBodyCreator(child, action)).create();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private static String messageBodyCreator(Children child, ChildAccoutActionType action) {
		String messageBody = "";
		switch (action) {
		case ADD:
			messageBody = "Hi, you have been added to Earnit, Download "
					+ "iOS: "+AppConstants.IOS_APPSTORE+" OR Android: "+AppConstants.ANDROID_PLAYSTORE+", use Email:" + child.getEmail()
					+ " and password: " + child.getPassword() + " to login";
			return messageBody;
		case UPDATE:
			messageBody = "Hi, your Earnit account have been updated , Download "
					+ "iOS: "+AppConstants.IOS_APPSTORE+" OR Android: "+AppConstants.ANDROID_PLAYSTORE+", use Email:" + child.getEmail()
					+ " and password: " + child.getPassword() + " to login";
			return messageBody;
		}
		return messageBody;

	}

}