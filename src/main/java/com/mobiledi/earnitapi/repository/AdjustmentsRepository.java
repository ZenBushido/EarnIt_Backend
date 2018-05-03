package com.mobiledi.earnitapi.repository;

import com.mobiledi.earnitapi.domain.Adjustments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sunil Gulabani on 29-12-2017.
 */
@Repository
public interface AdjustmentsRepository extends CrudRepository<Adjustments, Integer> {
}
