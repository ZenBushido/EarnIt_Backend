package com.mobiledi.earnitapi.repository.custom;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.util.AppConstants.ChildAccoutActionType;
import com.mobiledi.earnitapi.util.SMSUtility;

@Slf4j
@Repository
public class ChildrenRepositoryImpl implements ChildrenRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@Override
	public Children persistChild(Children child) {
		child.setCreateDate(new Timestamp(new DateTime().getMillis()));
		entityManager.persist(child);
		log.info("Message Sending success? :" + SMSUtility.SendSMS(child, ChildAccoutActionType.UPDATE));
		return child;
	}

	@Transactional
	@Override
	public Children findChild(int id) {
		return entityManager.find(Children.class, id);
	}

}
