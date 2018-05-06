package com.mobiledi.earnit.earnitapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobiledi.earnitapi.domain.Account;
import com.mobiledi.earnitapi.domain.Adjustments;
import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.DayTaskStatus;
import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.RepititionSchedule;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.util.AppConstants;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Sunil Gulabani on 23-12-2017.
 */
public class DummyData {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    private static final String URL =
//            "http://35.162.48.144:8080/earnit-api/";
            "http://localhost:9191/";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final String PARENT_EMAIL = "parent" + Math.random();
    public static final String PARENT_PASSWORD = "1234";

    public static final String CHILD_EMAIL = "child" + Math.random();
    public static final String CHILD_PASSWORD = "1234";

    public static final String CHILD_EMAIL_2 = "child" + Math.random();
    public static final String CHILD_PASSWORD_2 = "1234";

    public static void main(String[] args) {
        Parent parent = createParent();
        Children children = createChild(parent);
        Children children2 = createChild2(parent);

        getAllChildren(children.getAccount());
        deleteChild(children);
        getAllChildren(children.getAccount());

        Goal goal = createGoal(children.getId());
        getAllGoals(children.getId());
        deleteGoal(goal.getId());
        getAllGoals(children.getId());

        goal = createGoal(children.getId());
        goal = goalAdjustments(goal.getId());
        Task task = createTask(goal.getId(), children.getId(), null);

        task = createTask(goal.getId(), children.getId(), createDailyRepeatTask());

        getAllTask(children.getId());
        task = updateTask(task, goal.getId(), children.getId());
        getAllTask(children.getId());

        task = createTask(goal.getId(), children.getId(), createWeeklyRepeatTask());
        task = createTask(goal.getId(), children.getId(), createMonthlyRepeatTask());

        reassignTask(task, goal.getId(), children.getId());
    }

    private static Goal goalAdjustments(Integer goalId) {
        System.out.println("------------------------------------------");
        Adjustments adjustments = new Adjustments();
        Goal goal = new Goal();
        goal.setId(goalId);
        adjustments.setGoal(goal);
        adjustments.setAmount(100);
        adjustments.setReason("Dummy reason");

        String json = gson.toJson(adjustments);
        System.out.println("Goal Adjustment Request Body: " + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());
        ResponseEntity<Goal> responseEntity = getRestTemplateForParent().exchange(
                URL + "adjustments",
                HttpMethod.POST, requestEntity, Goal.class);

        System.out.println("Goal Adjustment Response Body: " + gson.toJson(responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private static List<Task> getAllTask(Integer childId) {
        System.out.println("------------------------------------------");
        ResponseEntity<List> responseEntity = getRestTemplateForParent().exchange(
                URL + "tasks/" + childId,
                HttpMethod.GET, null, List.class);

        System.out.println(gson.toJson(responseEntity.getBody()));

        return responseEntity.getBody();
    }

    private static Task updateTask(Task task, Integer goalId, Integer childrenId) {
        System.out.println("------------------------------------------");
        Children children = new Children();
        children.setId(childrenId);
        task.setChildren(children);

        Goal goal = new Goal();
        goal.setId(goalId);
        task.setGoal(goal);

        List<DayTaskStatus> dayTaskStatuses = task.getRepititionSchedule().getDayTaskStatuses();
        if(dayTaskStatuses == null) {
            dayTaskStatuses = new ArrayList<>();
        }

        DayTaskStatus dayTaskStatus = new DayTaskStatus();
        dayTaskStatus.setCreatedDateTime(new Timestamp(Instant.now().toEpochMilli()));
        dayTaskStatus.setStatus(AppConstants.TASK_COMPLETED);

        RepititionSchedule repititionSchedule = new RepititionSchedule();
        repititionSchedule.setId(task.getRepititionSchedule().getId());
        dayTaskStatus.setRepititionSchedule(repititionSchedule);
        dayTaskStatuses.add(dayTaskStatus);
        task.getRepititionSchedule().setDayTaskStatuses(dayTaskStatuses);

        String json = gson.toJson(task);
        System.out.println("Update Task Request Body: " + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());
        ResponseEntity<Task> responseEntity = getRestTemplateForParent().exchange(
                URL + "tasks",
                HttpMethod.PUT, requestEntity, Task.class);

        System.out.println("Update Task Response Body: " + gson.toJson(responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private static Task reassignTask(Task task, Integer goalId, Integer childrenId) {
        System.out.println("------------------------------------------");
        Children children = new Children();
        children.setId(childrenId);
        task.setChildren(children);

        Goal goal = new Goal();
        goal.setId(goalId);
        task.setGoal(goal);

        task.setStatus(AppConstants.TASK_REASSIGNED);

        String json = gson.toJson(task);
        System.out.println("Reassign (Update) Task Request Body: " + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());
        ResponseEntity<Task> responseEntity = getRestTemplateForParent().exchange(
                URL + "tasks",
                HttpMethod.PUT, requestEntity, Task.class);

        System.out.println("Reassign (Update) Task Response Body: " + gson.toJson(responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private static RepititionSchedule createMonthlyRepeatTask() {
        RepititionSchedule repititionSchedule = new RepititionSchedule();
        repititionSchedule.setRepeat("monthly");
        repititionSchedule.setStartTime("10:30");
        repititionSchedule.setEndTime("11:45");

        Set<String> days = new HashSet<>();
        days.add("1");
        days.add("5");
        days.add("12");
        days.add("16");
        days.add("18");
        days.add("20");
        days.add("26");
        days.add("28");
        repititionSchedule.setSpecificDays(days);
        return repititionSchedule;
    }

    private static RepititionSchedule createWeeklyRepeatTask() {
        RepititionSchedule repititionSchedule = new RepititionSchedule();
        repititionSchedule.setRepeat("weekly");
        repititionSchedule.setStartTime("00:00");
        repititionSchedule.setEndTime("23:59");

        Set<String> days = new HashSet<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Sunday");
        repititionSchedule.setSpecificDays(days);
        return repititionSchedule;
    }

    private static RepititionSchedule createDailyRepeatTask() {
        RepititionSchedule repititionSchedule = new RepititionSchedule();
        repititionSchedule.setRepeat("daily");
        repititionSchedule.setStartTime("10:00");
        repititionSchedule.setEndTime("11:00");
        return repititionSchedule;
    }

    private static Task createTask(Integer goalId, Integer childrenId, RepititionSchedule repititionSchedule) {
        System.out.println("------------------------------------------");
        Task task = getTaskObject(goalId, childrenId);
        task.setRepititionSchedule(repititionSchedule);

        String json = gson.toJson(task);
        System.out.println("Task Request Body: " + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());
        ResponseEntity<Task> responseEntity = getRestTemplateForParent().exchange(
                URL + "tasks",
                HttpMethod.POST, requestEntity, Task.class);

        System.out.println("Task Response Body: " + gson.toJson(responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private static Goal createGoal(Integer childrenId) {
        System.out.println("------------------------------------------");
        Goal goal = new Goal();
        goal.setName("Goal - " + new Date());
        goal.setAmount(BigDecimal.valueOf(1000));
        Children children = new Children();
        children.setId(childrenId);
        goal.setChildren(children);

        String json = gson.toJson(goal);
        System.out.println("Goal Request Body: \n" + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());
        ResponseEntity<Goal> responseEntity = getRestTemplateForParent().exchange(
                URL + "goals",
                HttpMethod.POST, requestEntity, Goal.class);

        System.out.println("Goal Response Body:\n" + gson.toJson(responseEntity.getBody()));

        return responseEntity.getBody();
    }

    private static void deleteGoal(Integer goalId) {
        System.out.println("------------------------------------------");
        ResponseEntity<Object> responseEntity = getRestTemplateForParent().exchange(
                URL + "goals/" + goalId,
                HttpMethod.DELETE, null, Object.class);

        System.out.println("Delete Goal:\n" + gson.toJson(responseEntity.getBody()));
    }

    private static void getAllGoals(Integer childId) {
        System.out.println("------------------------------------------");
        ResponseEntity<List> responseEntity = getRestTemplateForParent().exchange(
                URL + "goals/" + childId,
                HttpMethod.GET, null, List.class);

        System.out.println("Get All Goals:\n" + gson.toJson(responseEntity.getBody()));
    }

    private static Task getTaskObject(Integer goalId, Integer childrenId) {
        Task task = new Task();
        Children children = new Children();
        children.setId(childrenId);
        task.setChildren(children);
        task.setAllowance(BigDecimal.valueOf(100));
        task.setDeleted(false);
        task.setDescription("Dummy Task 1");
        task.setDueDate(new Timestamp(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()));
        task.setName("Dummy Task 1");
        Goal goal = new Goal();
        goal.setId(goalId);
        task.setGoal(goal);
        task.setShouldLockAppsIfTaskOverdue(true);
        return task;
    }

    private static void getAllChildren(Account account) {
        System.out.println("------------------------------------------");
        ResponseEntity<List> responseEntity = getRestTemplateForParent().exchange(
                URL + "childrens/" + account.getId(),
                HttpMethod.GET, null, List.class);

        System.out.println(gson.toJson(responseEntity.getBody()));
    }

    private static void deleteChild(Children children) {
        System.out.println("------------------------------------------");
        ResponseEntity<Object> responseEntity = getRestTemplateForParent().exchange(
                URL + "childrens/" + children.getId(),
                HttpMethod.DELETE, null, Object.class);

        System.out.println("Delete Child Response:\n" + gson.toJson(responseEntity.getBody()));
    }

    private static Children createChild(Parent parent) {
        return createChild(parent, CHILD_EMAIL, CHILD_PASSWORD);
    }

    private static Children createChild2(Parent parent) {
        return createChild(parent, CHILD_EMAIL_2, CHILD_PASSWORD_2);
    }

    private static Children createChild(Parent parent, String email, String password) {
        System.out.println("------------------------------------------");
        Account parentAccount = parent.getAccount();
        Account childAccount = new Account();
        childAccount.setId(parentAccount.getId());

        Children children = new Children();
        children.setAccount(childAccount);
        children.setEmail(email);
        children.setPassword(password);
        children.setPhone("+919714888118");
        children.setFirstName(email);

        String json = gson.toJson(children);
        System.out.println("Create Children Request Body: \n" + json);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());

        ResponseEntity<Children> responseEntity = getRestTemplateForParent().exchange(
                URL + "signup/child",
                HttpMethod.POST, requestEntity, Children.class);

        System.out.println("Create Children Response Body: \n" + gson.toJson(responseEntity.getBody()));

        return responseEntity.getBody();
    }

    private static Parent createParent() {
        System.out.println("------------------------------------------");
        Parent parent = new Parent();
        parent.setEmail(PARENT_EMAIL);
        parent.setPassword(PARENT_PASSWORD);

        String json = gson.toJson(parent);
        System.out.println("Create Parent Request Body: \n" + json);

        HttpEntity<String> requestEntity = new HttpEntity<>(json, getHttpHeaders());

        ResponseEntity<Parent> responseEntity = getRestTemplate().exchange(
                URL + "signup/parent",
                HttpMethod.POST, requestEntity, Parent.class);

        System.out.println("Create Parent Response Body: \n" + gson.toJson(responseEntity.getBody()));

        return responseEntity.getBody();
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static RestTemplate getRestTemplate() {
        return getRestTemplate(null, null);
    }

    private static RestTemplate getRestTemplateForParent() {
        return getRestTemplate(PARENT_EMAIL, PARENT_PASSWORD);
    }

    private static RestTemplate getRestTemplateForChild() {
        return getRestTemplate(CHILD_EMAIL, CHILD_PASSWORD);
    }

    private static RestTemplate getRestTemplate(String username, String password) {
        RestTemplate restTemplate = null;

        if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            HttpHost host = new HttpHost("localhost", 9191, "http");
            restTemplate = new RestTemplate(
                    new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
            restTemplate.getInterceptors().add(
                    new BasicAuthorizationInterceptor(username, password));
        } else {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
    }
}
