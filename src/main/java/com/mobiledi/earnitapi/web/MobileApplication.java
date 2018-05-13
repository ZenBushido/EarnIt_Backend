package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobileapplications")
public class MobileApplication {

  @PutMapping
  public String createMobile(@RequestBody MobileApplicationRequestDto requestDto) {
    System.out.println(requestDto);
    return "Ok";
  }

}
