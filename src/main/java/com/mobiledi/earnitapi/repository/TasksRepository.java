package com.mobiledi.earnitapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Task;

public interface TasksRepository extends CrudRepository<Task, Integer> {
	List<Task> findByChildrenIdAndIsDeleted(int id, boolean isDeleted);
}
