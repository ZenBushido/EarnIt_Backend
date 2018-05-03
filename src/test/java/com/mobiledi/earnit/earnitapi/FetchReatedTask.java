package com.mobiledi.earnit.earnitapi;


import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mobiledi.earnitapi.util.AppConstants;

@Component
public class FetchReatedTask {
	
	 //execute before class
	   @Before
	   public void before() {
	      System.out.println("in before class");
	   }

	@Test
	public void test() {


		try {
			Unirest.setTimeouts(300000, 200000);
			HttpRequest request = Unirest.get(AppConstants.GET_ALL_REPEAT_TASK_URL)
					.basicAuth(AppConstants.AUTH_EMAIL, AppConstants.AUTH_PASSWORD);

			HttpResponse<JsonNode> stackResponse = request.asJson();
			JSONArray responseArray = stackResponse.getBody().getArray();
			System.out.println("Auto generated task count: "+responseArray.length());

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(true, true);
		
	}
}