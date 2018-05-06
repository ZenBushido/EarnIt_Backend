package com.mobiledi.earnitapi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


// Researching on how to make H2 as test db.
//@SpringBootTest(classes = {EarnitApiApplication.class})
//@RunWith(SpringRunner.class)
public class BaseIT {

  @Autowired
  ApplicationContext applicationContext;

  //@Test
  public void WHEN_app_is_bootstrap_VERIFY_application_context_gets_Created(){
    Assert.assertNotNull(applicationContext);
  }

}
