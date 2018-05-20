package com.mobiledi.earnitapi.services.impl;

import com.mobiledi.earnitapi.domain.Goal;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.services.GoalServiceCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoalServiceCustomImpl implements GoalServiceCustom {

  @Override
  public boolean checkIfGoalReached(Goal goal) {
    if (goal == null) {
      return false;
    }

    BigDecimal tally = new BigDecimal(0);
    for (Task task : goal.getTasks()) {
      if (task.getStatus().equalsIgnoreCase(AppConstants.TASK_CLOSED)) {
        tally = tally.add(task.getAllowance());
      }
    }

    if (tally.compareTo(goal.getAmount()) > 1) {
      log.info("Goal " + goal.getName() + "has completed");
      return true;
    } else {
      log.info("Goal " + goal.getName() + " is yet to be completed");

      return false;
    }
  }
}
