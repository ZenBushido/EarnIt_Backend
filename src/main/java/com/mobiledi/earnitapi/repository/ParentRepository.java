package com.mobiledi.earnitapi.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Parent;

public interface ParentRepository extends CrudRepository<Parent, Integer> {

	Parent findParentById(Integer id);
	List<Parent> findParentByIsPasswordEncryptedIsNull();
}
