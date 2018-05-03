package com.mobiledi.earnitapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.TaskComment;

public interface TaskCommentRepository extends CrudRepository<TaskComment, Long> {
}
