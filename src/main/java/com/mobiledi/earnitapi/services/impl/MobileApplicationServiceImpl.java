package com.mobiledi.earnitapi.services.impl;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.MobileApplication;
import com.mobiledi.earnitapi.domain.MobileApplicationUsage;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.MobileApplicationRepository;
import com.mobiledi.earnitapi.services.MobileApplicationService;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileApplicationServiceImpl implements MobileApplicationService {

  @Autowired
  private MobileApplicationRepository mobileApplicationRepository;

  @Autowired
  private ChildrenRepository childrenRepository;

  @Transactional
  @SneakyThrows
  public void persist(MobileApplicationRequestDto mobileApplicationRequestDto, Children children) {

    List<MobileApplicationUsage> mobileApplicationUsages = convertToMobileApplicationUsageList(
        mobileApplicationRequestDto);

    MobileApplication mobileApplication = MobileApplication.builder()
        .createdDate(Timestamp.from(Instant.now()))
        .children(childrenRepository.findById(children.getId()).get())
        .name(mobileApplicationRequestDto.getName())
        .mobileApplicationUsages(mobileApplicationUsages)
        .build();

    mobileApplicationUsages.forEach(mobileApplicationUsage -> mobileApplicationUsage.setMobileApplication(mobileApplication));

    mobileApplicationRepository.save(mobileApplication);
  }

  @Transactional
  @SneakyThrows
  public void update(MobileApplicationRequestDto mobileApplicationRequestDto, Children children) {
    List<MobileApplicationUsage> mobileApplicationUsages = convertToMobileApplicationUsageList(
        mobileApplicationRequestDto);

    MobileApplication mobileApplication = mobileApplicationRepository
        .findByNameAndChildrenId(mobileApplicationRequestDto.getName(), children.getId()).get(0);
    mobileApplication.getMobileApplicationUsages().addAll(mobileApplicationUsages);

    mobileApplicationUsages.forEach(mobileApplicationUsage -> mobileApplicationUsage.setMobileApplication(mobileApplication));

    mobileApplicationRepository.save(mobileApplication);
  }

  public boolean doesApplicationExist(String name, Children children) {
    return mobileApplicationRepository.countByNameAndChildrenId(name, children.getId()) > 0;
  }

  private List<MobileApplicationUsage> convertToMobileApplicationUsageList(
      MobileApplicationRequestDto mobileApplicationRequestDto) {
    return mobileApplicationRequestDto.getUsage()
        .stream().map(mobileApplicationUsageDto ->
            MobileApplicationUsage.builder()
                .endTime(Timestamp.from(mobileApplicationUsageDto.getEnd()))
                .startTime(Timestamp.from(mobileApplicationUsageDto.getStart()))
                .build()
        ).collect(Collectors.toList());
  }

}
