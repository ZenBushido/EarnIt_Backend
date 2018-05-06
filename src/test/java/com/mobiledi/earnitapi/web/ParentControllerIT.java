package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.BaseIT;
import com.mobiledi.earnitapi.repository.ParentRepository;
import com.mobiledi.earnitapi.repository.TasksRepository;
import com.mobiledi.earnitapi.services.GoalService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParentControllerIT extends BaseIT {

  @Autowired
  ParentRepository parentRepository;

  //@Test
  public void test(){
    Assert.assertNull(parentRepository.findById(1L).get());
  }

}