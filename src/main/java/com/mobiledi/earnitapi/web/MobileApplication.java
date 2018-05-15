package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import com.mobiledi.earnitapi.services.MobileApplicationService;
import com.mobiledi.earnitapi.util.AuthenticatedUserProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobileapplications")
public class MobileApplication {

  @Autowired
  private MobileApplicationService mobileApplicationService;

  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @PutMapping
  @SneakyThrows
  public String createMobile(@RequestBody MobileApplicationRequestDto requestDto) {
    Children children = authenticatedUserProvider.getLoggedInChild();
    mobileApplicationService.persist(requestDto, children);
    return "Ok";
  }

}
