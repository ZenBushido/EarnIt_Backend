package com.mobiledi.earnitapi.services;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import java.util.List;

public interface MobileApplicationService {

  void persist(List<MobileApplicationRequestDto> mobileApplicationRequestList, Children children);

  void persist(MobileApplicationRequestDto mobileApplicationRequestDto, Children children);

  void update(MobileApplicationRequestDto mobileApplicationRequestDto, Children children);

  boolean doesApplicationExist(String name, Integer childrenId);

  void markTheAppIgnored(String name, Integer childrenId);
}
