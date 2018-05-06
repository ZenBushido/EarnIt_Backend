package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.QChildren;
import com.mobiledi.earnitapi.domain.QParent;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountRepositoryImpl implements AccountRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Parent findParentbyemailandpassword(String email, String password) {
		log.info("Trying to login a parent " + email);
		QParent parent = QParent.parent;

		JPAQuery<?> query = new JPAQuery<Void>(entityManager);

		Parent parentObject = query.select(parent).from(parent)
				.where(parent.email.equalsIgnoreCase(email).and(parent.password.eq(password))).fetchOne();
		return parentObject;
	}

	@Override
	public Children findChildbyemailandpassword(String email, String password) {
		log.info("Trying to login a child " + email);
		QChildren child = QChildren.children;

		JPAQuery<?> query = new JPAQuery<Void>(entityManager);

		Children childObject = query.select(child).from(child)
				.where(child.email.equalsIgnoreCase(email).and(child.password.eq(password))).fetchOne();
		return childObject;
	}

	@Override
	public Parent findParentByEmail(String email) {
		QParent parent = QParent.parent;
		log.info("Trying to validate as a Parent user...");

		JPAQuery<?> query = new JPAQuery<Void>(entityManager);
		Parent parentObject = query.select(parent).from(parent).where(parent.email.equalsIgnoreCase(email)).fetchOne();
		return parentObject;
	}

	@Override
	public Children findChildByEmail(String email) {
		QChildren child = QChildren.children;

		log.info("Trying validate as a Child user...XX");

		JPAQuery<?> query = new JPAQuery<Void>(entityManager);
		Children childObject = query.select(child).from(child).where(child.email.equalsIgnoreCase(email)).fetchOne();
		return childObject;
	}

}
