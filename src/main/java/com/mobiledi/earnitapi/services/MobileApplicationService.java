package com.mobiledi.earnitapi.services;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;

public interface MobileApplicationService {

  void persist(MobileApplicationRequestDto mobileApplicationRequestDto, Children children);

  void update(MobileApplicationRequestDto mobileApplicationRequestDto, Children children);

  boolean doesApplicationExist(String name, Children children);
}
