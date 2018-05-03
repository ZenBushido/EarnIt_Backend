package com.mobiledi.earnitapi.repository.custom;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobiledi.earnitapi.domain.Account;
import com.mobiledi.earnitapi.domain.Parent;

@Repository
public class ParentRepositoryImpl implements ParentRepositoryCustom {
	private static Log logger = LogFactory.getLog(ParentRepositoryImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public Parent persistParent(Parent parent) {
		Account account = new Account();
		account.setAccountCode(RandomStringUtils.randomAlphanumeric(7).toUpperCase());
		account.setCreateDate(new Timestamp(new DateTime().getMillis()));
		entityManager.persist(account);

		parent.setFirstName(" ");
		parent.setCreateDate(new Timestamp(new DateTime().getMillis()));
		parent.setAccount(account);
		entityManager.persist(parent);

		return parent;
	}

}
