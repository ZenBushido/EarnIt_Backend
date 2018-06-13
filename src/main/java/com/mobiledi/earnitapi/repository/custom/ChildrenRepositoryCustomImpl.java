package com.mobiledi.earnitapi.repository.custom;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.util.AppConstants.ChildAccoutActionType;
import com.mobiledi.earnitapi.util.SMSUtility;

@Slf4j
@Repository
public class ChildrenRepositoryCustomImpl implements ChildrenRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public Children persistChild(Children child) {
		child.setCreateDate(new Timestamp(new DateTime().getMillis()));
		child.setDeleted(false);
		entityManager.persist(child);
		log.info("Message Sending success? :" + SMSUtility.SendSMS(child, ChildAccoutActionType.UPDATE));
		return child;
	}

	@Transactional
	@Override
	public Children findChild(int id) {
		return entityManager.find(Children.class, id);
	}

	@Transactional
	@Override
	public Children updateChild(Children child) {
		Children lastChildRecord = findChild(child.getId());
		String lastUpdatedNumber = lastChildRecord.getPhone();

		child.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		if (lastChildRecord != null) {
			log.info("previous phone number is : " + lastUpdatedNumber);
			log.info("updating phone number to : " + child.getPhone());
			if (!lastUpdatedNumber.equalsIgnoreCase(child.getPhone())) {
				log.info(
						"Message Sending success? :" + SMSUtility.SendSMS(child, ChildAccoutActionType.ADD));
			}
		}

		if (StringUtils.isNotBlank(child.getAvatar())) {
			lastChildRecord.setAvatar(child.getAvatar());
		}

		if (StringUtils.isNotBlank(child.getEmail())) {
			lastChildRecord.setEmail(child.getEmail());
		}

		if (StringUtils.isNotBlank(child.getFirstName())) {
			lastChildRecord.setFirstName(child.getFirstName());
		}

		if (StringUtils.isNotBlank(child.getLastName())) {
			lastChildRecord.setLastName(child.getLastName());
		}

		if (StringUtils.isNotBlank(child.getFcmToken())) {
			lastChildRecord.setFcmToken(child.getFcmToken());
		}

		if (StringUtils.isNotBlank(child.getMessage())) {
			lastChildRecord.setMessage(child.getMessage());
		}

		if (StringUtils.isNotBlank(child.getPhone())) {
			lastChildRecord.setPhone(child.getPhone());
		}

		return entityManager.merge(lastChildRecord);
	}

}
