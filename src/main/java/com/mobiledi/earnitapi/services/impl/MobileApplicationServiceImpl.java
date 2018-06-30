package com.mobiledi.earnitapi.services.impl;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.MobileApplication;
import com.mobiledi.earnitapi.domain.MobileApplicationUsage;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.MobileApplicationRepository;
import com.mobiledi.earnitapi.repository.MobileApplicationUsageRepository;
import com.mobiledi.earnitapi.services.MobileApplicationService;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
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
  private MobileApplicationUsageRepository mobileApplicationUsageRepository;

  @Autowired
  private ChildrenRepository childrenRepository;

  @Transactional
  @SneakyThrows
  public void persist(List<MobileApplicationRequestDto> mobileApplicationRequestList,
      Children children) {
    mobileApplicationRequestList.forEach(mobileApplicationRequestDto -> {
      boolean doesApplicationExist = doesApplicationExist(mobileApplicationRequestDto.getName(),
          children.getId());
      if (doesApplicationExist) {
        update(mobileApplicationRequestDto, children);
      } else {
        persist(mobileApplicationRequestDto, children);
      }
    });

  }

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
        .ignoredByParent(false)
        .build();

    mobileApplicationUsages.forEach(
        mobileApplicationUsage -> mobileApplicationUsage.setMobileApplication(mobileApplication));

    mobileApplicationRepository.save(mobileApplication);
  }

  @Transactional
  private boolean doesAppUsageExist(String appName, Integer childId, Timestamp startTime,
      Timestamp endTime) {
    return mobileApplicationUsageRepository
        .countByMobileApplicationNameAndMobileApplicationChildrenIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            appName,
            childId,
            startTime,
            endTime
        ) == 0;
  }

  @Transactional
  @SneakyThrows
  public void update(MobileApplicationRequestDto mobileApplicationRequestDto, Children children) {
    List<MobileApplicationUsage> mobileApplicationUsages = convertToMobileApplicationUsageList(
        mobileApplicationRequestDto);

    mobileApplicationUsages = mobileApplicationUsages.stream()
        .filter(mobileApplicationUsage -> doesAppUsageExist(
            mobileApplicationRequestDto.getName(),
            children.getId(),
            mobileApplicationUsage.getStartTime(),
            mobileApplicationUsage.getEndTime()
        )).collect(Collectors.toList());

    MobileApplication mobileApplication = mobileApplicationRepository
        .findByNameAndChildrenId(mobileApplicationRequestDto.getName(), children.getId()).get(0);
    mobileApplication.getMobileApplicationUsages().addAll(mobileApplicationUsages);

    mobileApplicationUsages.forEach(
        mobileApplicationUsage -> mobileApplicationUsage.setMobileApplication(mobileApplication));

    mobileApplicationRepository.save(mobileApplication);
  }

  public boolean doesApplicationExist(String name, Integer childrenId) {
    return mobileApplicationRepository.countByNameAndChildrenId(name, childrenId) > 0;
  }

  @Override
  public void markTheAppIgnored(String name, Integer childrenId) {
    MobileApplication mobileApplication = mobileApplicationRepository
        .findByNameAndChildrenId(name, childrenId).get(0);
    mobileApplication.setIgnoredByParent(true);
    mobileApplicationRepository.save(mobileApplication);
  }

  private List<MobileApplicationUsage> convertToMobileApplicationUsageList(
      MobileApplicationRequestDto mobileApplicationRequestDto) {
    return mobileApplicationRequestDto.getUsage()
        .stream().map(mobileApplicationUsageDto -> {
          OffsetDateTime start = OffsetDateTime
              .parse(mobileApplicationUsageDto.getStartDateTimeWithZone());
          OffsetDateTime end = OffsetDateTime
              .parse(mobileApplicationUsageDto.getEndDateTimeWithZone());
          return MobileApplicationUsage.builder()
              .endTime(Timestamp.from(end.toInstant()))
              .startTime(Timestamp.from(start.toInstant()))
              .startWithZone(mobileApplicationUsageDto.getStartDateTimeWithZone())
              .endWithZone(mobileApplicationUsageDto.getEndDateTimeWithZone())
              .durationInMinute(
                  Duration.between(start, end).toMinutes())
              .build();
        }).collect(Collectors.toList());
  }

}
