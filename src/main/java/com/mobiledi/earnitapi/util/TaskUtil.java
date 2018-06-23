package com.mobiledi.earnitapi.util;

import com.mobiledi.earnitapi.domain.Task;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TaskUtil {

  public List<Task> filterClosedTasks(List<Task> tasks) {
    return tasks.stream()
        .filter(task -> !task.getStatus().equals(AppConstants.TASK_CLOSED))
        .filter(task-> !task.isDeleted())
        .collect(Collectors.toList());
  }

}
