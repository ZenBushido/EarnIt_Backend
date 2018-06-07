package com.mobiledi.earnitapi.repository;

import com.mobiledi.earnitapi.domain.MobileApplicationUsage;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MobileApplicationUsageRepository extends
    CrudRepository<MobileApplicationUsage, Long> {

  List<MobileApplicationUsage> findByMobileApplicationIgnoredByParentAndMobileApplicationChildrenIdAndStartTimeGreaterThanAndStartTimeLessThan(
      Boolean ignoreByParent, Integer childId, Timestamp start, Timestamp end);

  List<MobileApplicationUsage> findByMobileApplicationIgnoredByParentAndMobileApplicationNameAndMobileApplicationChildrenIdAndStartTimeGreaterThanAndStartTimeLessThan(
      Boolean ignoreByParent, String name, Integer childId, Timestamp start, Timestamp end);

  Long countByMobileApplicationNameAndMobileApplicationChildrenIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
      String name, Integer childId, Timestamp start, Timestamp end);

}