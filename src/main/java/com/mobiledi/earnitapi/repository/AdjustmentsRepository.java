package com.mobiledi.earnitapi.repository;

import com.mobiledi.earnitapi.domain.Adjustments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentsRepository extends CrudRepository<Adjustments, Integer> {
}
