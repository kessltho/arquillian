package com.zuehlke.arquillian;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MyBean {

	@Inject
	private MyPrinter p;

	@PersistenceContext()
	private EntityManager em;

	private PersonEntity person;

	@RolesAllowed("Admin")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void soSomethingCoolAndFancy() {
		p.print("Person.name=" + person.getName());
	}

	@PostConstruct
	public void initBean() {
		this.person = em.find(PersonEntity.class, 1);
	}
}
