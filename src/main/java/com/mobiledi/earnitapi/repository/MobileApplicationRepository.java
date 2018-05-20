package com.mobiledi.earnitapi.repository;

import com.mobiledi.earnitapi.domain.MobileApplication;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MobileApplicationRepository extends CrudRepository<MobileApplication, Long> {

  List<MobileApplication> findByNameAndChildrenId(String name, Integer id);
  Long countByNameAndChildrenId(String name, Integer id);
}
