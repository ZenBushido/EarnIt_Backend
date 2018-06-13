package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.repository.ParentRepository;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobiledi.earnitapi.domain.Account;
import com.mobiledi.earnitapi.domain.Parent;

@Repository
public class ParentRepositoryCustomImpl implements ParentRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	ParentRepository parentRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

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

	@Override
	public Parent updateParent(Parent parent) {
		Parent lastParentRecord = parentRepository.findById (parent.getId()).get();

		parent.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		if (StringUtils.isNotBlank(parent.getAvatar())) {
			lastParentRecord.setAvatar(parent.getAvatar());
		}

		if (StringUtils.isNotBlank(parent.getEmail())) {
			lastParentRecord.setEmail(parent.getEmail());
		}

		if (StringUtils.isNotBlank(parent.getFirstName())) {
			lastParentRecord.setFirstName(parent.getFirstName());
		}

		if (StringUtils.isNotBlank(parent.getLastName())) {
			lastParentRecord.setLastName(parent.getLastName());
		}

		if (StringUtils.isNotBlank(parent.getFcmToken())) {
			lastParentRecord.setFcmToken(parent.getFcmToken());
		}

		if (StringUtils.isNotBlank(parent.getPhone())) {
			lastParentRecord.setPhone(parent.getPhone());
		}

		return parentRepository.save(lastParentRecord);
	}

}
