package com.mobiledi.earnitapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Children;

public interface ChildrenRepository extends CrudRepository<Children, Integer> {

	List<Children> findChildrenByAccountIdOrderByFirstNameAsc(int id);
	List<Children> findChildrenByAccountIdAndIsDeletedOrderByFirstNameAsc(int id, boolean isDeleted);
	List<Children> findChildrenByIsPasswordEncryptedIsNull();
}
