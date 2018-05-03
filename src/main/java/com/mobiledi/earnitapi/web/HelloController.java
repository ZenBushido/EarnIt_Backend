package com.mobiledi.earnitapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobiledi.earnitapi.repository.HelloWorldService;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;

@Controller
public class HelloController {

	@Autowired
	private HelloWorldService helloWorldService;

	@GetMapping("/hello")
	@ResponseBody
	public String helloWorld() {
		// PushNotifier.sendPushNotification("",
		// NotificationCategory.TASK_UPDATED, "messageBody");

		return this.helloWorldService.getHelloMessage();
	}

	@GetMapping("/sendPush/{dev_id}")
	@ResponseBody
	public boolean sendPush(@PathVariable String dev_id) {
		return PushNotifier.sendPushNotification(0, dev_id, NotificationCategory.TASK_CREATED,
				"This is a sample task title");
	}

}