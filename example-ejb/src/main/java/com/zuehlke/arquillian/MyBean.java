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
	EchoService echoService;

	@PersistenceContext
	EntityManager em;

	PersonEntity person;

	@RolesAllowed("Admin")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String echoPerson() {
		return echoService.echo(person.getName());
	}

	@PostConstruct
	public void initBean() {
		this.person = em.find(PersonEntity.class, 1);
	}
}
