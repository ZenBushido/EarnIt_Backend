package com.mobiledi.earnitapi.repository.custom;

import java.util.List;

import com.mobiledi.earnitapi.domain.Task;

public interface TaskRepositoryCustom {
	
	List<Task> fetchRepeatTask();

}
