package com.mobiledi.earnitapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Parent;

public interface ParentRepository extends CrudRepository<Parent, Long> {

	Parent findParentById(Long id);
}
