package com.mobiledi.earnitapi.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.util.AppConstants;

@Slf4j
@Component
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 5 0 * * ?")
    public void reportCurrentTime(){
    	log.info("triggered auto generate repeat task...."+ dateFormat.format(new Date()));
    	try {
			Unirest.setTimeouts(300000, 200000);
			HttpRequest request = Unirest.get(AppConstants.GET_ALL_REPEAT_TASK_URL)
					.basicAuth(AppConstants.AUTH_EMAIL, AppConstants.AUTH_PASSWORD);

			HttpResponse<JsonNode> response = request.asJson();
			JSONArray responseArray = response.getBody().getArray();
			if (responseArray.length() > 0)
				log.info("Auto generated task count: "+responseArray.length());
			else
				log.info("No task to auto generate");

		} catch (UnirestException e) {
    		log.error("Error occurred : ", e);
		}
    }
}
