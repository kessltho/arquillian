package com.zuehlke.arquillian;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.math3.analysis.function.Abs;

@Stateless
public class BookshelfService {

	@PersistenceContext(unitName = "bookshelfManager")
	private EntityManager entityManager;

	@RolesAllowed({ "User", "Manager" })
	public void addBook(Book book) {
		new Abs().value(12);
		entityManager.persist(book);
	}

	@RolesAllowed({ "Manager" })
	public void deleteBook(Book book) {
		entityManager.remove(book);
	}

	@PermitAll
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Book> getBooks() {
		TypedQuery<Book> query = entityManager.createQuery("SELECT b from Book as b", Book.class);
		return query.getResultList();
	}
}
