package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.BaseIT;
import com.mobiledi.earnitapi.repository.ParentRepository;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class ParentControllerIT extends BaseIT {

  @Autowired
  ParentRepository parentRepository;

  //@Test
  public void test(){
    Assert.assertNull(parentRepository.findById(1).get());
  }

}