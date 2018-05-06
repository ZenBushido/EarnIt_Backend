package com.mobiledi.earnitapi.services;

import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.util.AppConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl implements GoalService {

  final private static Log LOGGER = LogFactory.getLog(GoalServiceImpl.class);

  @Override
  public boolean checkIfGoalReached(Goal goal) {
    if (goal == null) {
      return false;
    }
    double tally = 0;
    for (Task task : goal.getTasks()) {
      if (task.getStatus().equalsIgnoreCase(AppConstants.TASK_CLOSED)) {
        tally += task.getAllowance();
      }
    }

    if (tally >= goal.getAmount()) {
      LOGGER.info("Goal " + goal.getName() + "has completed");
      return true;
    } else {
      LOGGER.info("Goal " + goal.getName() + " is yet to be completed");
      return false;
    }
  }
}
