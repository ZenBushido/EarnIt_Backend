package com.mobiledi.earnitapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Goal;

public interface GoalRepository extends CrudRepository<Goal, Integer> {
	List<Goal> findByChildrenIdAndIsDeleted(int id, boolean isDeleted);
}
