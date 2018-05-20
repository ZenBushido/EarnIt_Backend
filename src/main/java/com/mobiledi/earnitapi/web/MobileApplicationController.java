package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.MobileApplicationUsage;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.dto.MobileApplicationUsageResponseDto;
import com.mobiledi.earnitapi.dto.IgnoreMobileAppDto;
import com.mobiledi.earnitapi.dto.MobileApplicationRequestDto;
import com.mobiledi.earnitapi.dto.MobileApplicationUsageDto;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.MobileApplicationUsageRepository;
import com.mobiledi.earnitapi.services.MobileApplicationService;
import com.mobiledi.earnitapi.util.AuthenticatedUserProvider;
import com.mobiledi.earnitapi.validator.ParentChildRelationValidator;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobileapplications")
public class MobileApplicationController {

  @Autowired
  private MobileApplicationService mobileApplicationService;

  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @Autowired
  private ChildrenRepository childrenRepository;

  @Autowired
  private ParentChildRelationValidator parentChildRelationValidator;

  @Autowired
  private MobileApplicationUsageRepository mobileApplicationUsageRepository;

  @Value("${mobile.app.usage.days}")
  private Integer usageDays;

  final private static String OK = "Ok";

  @PutMapping
  @SneakyThrows
  public String createMobile(@RequestBody MobileApplicationRequestDto requestDto) {
    validate(requestDto);
    Children children = authenticatedUserProvider.getLoggedInChild();
    boolean doesApplicationExist = mobileApplicationService
        .doesApplicationExist(requestDto.getName(), children.getId());
    if (doesApplicationExist) {
      mobileApplicationService.update(requestDto, children);
    } else {
      mobileApplicationService.persist(requestDto, children);
    }
    return OK;
  }

  @PutMapping("/markappignore")
  @SneakyThrows
  public String markAppIgnore(@RequestBody IgnoreMobileAppDto ignoreMobileAppDto) {
    validateIgnoreMobileAppDto(ignoreMobileAppDto);
    mobileApplicationService
        .markTheAppIgnored(ignoreMobileAppDto.getAppName(), ignoreMobileAppDto.getChildrenId());
    return OK;
  }

  @GetMapping("/usage")
  @SneakyThrows
  public List<MobileApplicationUsageResponseDto> getAllAppsUsage(
      @RequestParam(name = "childid") Integer childId,
      @RequestParam(value = "days", required = false) Integer days) {
    validateChildExist(childId);
    days = Objects.isNull(days) ? 1 : days;
    validateParentAndChild(childId);
    days = usageDays < days ? usageDays : days;
    Timestamp start = Timestamp
        .valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(days - 1));
    Timestamp end = Timestamp
        .valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

    Map<String, Long> appUsages = new HashMap<>();
    mobileApplicationUsageRepository
        .findByMobileApplicationIgnoredByParentAndMobileApplicationChildrenIdAndStartTimeGreaterThanAndStartTimeLessThan(
            false, childId, start, end).forEach(mobileApplicationUsage -> {
      if (appUsages.containsKey(mobileApplicationUsage.getMobileApplication().getName())) {
        Long usage = appUsages.get(mobileApplicationUsage.getMobileApplication().getName());
        appUsages.put(mobileApplicationUsage.getMobileApplication().getName(),
            usage + mobileApplicationUsage.getDurationInMinute());
      } else {
        appUsages.put(mobileApplicationUsage.getMobileApplication().getName(),
            mobileApplicationUsage.getDurationInMinute());
      }
    });
    List<MobileApplicationUsageResponseDto> response = new ArrayList<>();
    appUsages
        .forEach((appName, timeUsed) -> response.add(MobileApplicationUsageResponseDto.builder()
            .appName(appName)
            .timeUsed(timeUsed)
            .build()));
    return response;
  }

  @GetMapping("/{appName}/usage")
  @SneakyThrows
  public List<MobileApplicationUsageDto> getAppUsage(@PathVariable("appName") String appName,
      @RequestParam(name = "childid") Integer childId,
      @RequestParam(value = "days", required = false) Integer days) {
    validateChildExist(childId);
    days = Objects.isNull(days) ? 1 : days;
    validateChildExist(childId);
    validateApplicationExist(appName, childId);
    validateParentAndChild(childId);
    days = usageDays < days ? usageDays : days;
    Timestamp start = Timestamp
        .valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(days - 1));
    Timestamp end = Timestamp
        .valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    return mobileApplicationUsageRepository
        .findByMobileApplicationIgnoredByParentAndMobileApplicationNameAndMobileApplicationChildrenIdAndStartTimeGreaterThanAndStartTimeLessThan(
            false, appName, childId, start, end)
        .stream()
        .map(mobileApplicationUsage -> MobileApplicationUsageDto
            .builder()
            .startDateTimeWithZone(mobileApplicationUsage.getStartWithZone())
            .endDateTimeWithZone(mobileApplicationUsage.getEndWithZone())
            .build()).collect(
            Collectors.toList());
  }

  private void validate(MobileApplicationRequestDto requestDto) {
    if (Objects.isNull(requestDto.getName())) {
      throw new ValidationException("app name cannot be null", 400);
    }

    if (Objects.nonNull(requestDto.getUsage())) {
      requestDto.getUsage().stream().forEach(mobileApplicationUsageDto -> {
        if (Objects.isNull(mobileApplicationUsageDto.getEndDateTimeWithZone()) || Objects
            .isNull(mobileApplicationUsageDto.getEndDateTimeWithZone())) {
          throw new ValidationException("app name cannot be null", 400);
        } else {
          try {
            OffsetDateTime.parse(mobileApplicationUsageDto.getEndDateTimeWithZone());
            OffsetDateTime.parse(mobileApplicationUsageDto.getStartDateTimeWithZone());
          } catch (Exception e) {
            throw new ValidationException("Incorrect Date Format", 400);
          }
        }
      });
    }
  }

  private void validateIgnoreMobileAppDto(IgnoreMobileAppDto ignoreMobileAppDto) {
    validateChildExist(ignoreMobileAppDto.getChildrenId());
    validateApplicationExist(ignoreMobileAppDto.getAppName(), ignoreMobileAppDto.getChildrenId());
    validateParentAndChild(ignoreMobileAppDto.getChildrenId());
  }

  @SneakyThrows
  private void validateParentAndChild(Integer childId) {
    Parent parent = authenticatedUserProvider.getLoggedInParent();
    boolean isParentOfChild = parentChildRelationValidator
        .isParentOfGivenChild(parent.getId(), childId);
    if (!isParentOfChild) {
      new ValidationException("Provided child id : +" + childId
          + " doesn't belong the parent : " + parent.getId(), 400);
    }
  }

  private void validateChildExist(Integer childrenId) {
    Optional<Children> children = childrenRepository.findById(childrenId);
    if (!children.isPresent()) {
      throw new ValidationException(
          "Child with id : " + childrenId + " doesn't exists", 400);
    }
  }

  private void validateApplicationExist(String appName, Integer childrenId) {
    boolean doesApplicationExist = mobileApplicationService
        .doesApplicationExist(appName, childrenId);
    if (!doesApplicationExist) {
      throw new ValidationException(
          "App with name : " + appName + " doesn't exists with child id : " + childrenId, 400);
    }
  }

}
