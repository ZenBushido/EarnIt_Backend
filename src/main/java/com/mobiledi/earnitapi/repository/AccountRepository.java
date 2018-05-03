package com.mobiledi.earnitapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.mobiledi.earnitapi.domain.Account;
import com.mobiledi.earnitapi.repository.custom.AccountRepositoryCustom;

public interface AccountRepository extends CrudRepository<Account, Long>, AccountRepositoryCustom {
	Account findById(int id);
}
