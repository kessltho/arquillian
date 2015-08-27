package org.aquillian.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String isbn;
	private String title;

	public Book() {
	}

	public Book(String isbn, String title) {
		this.isbn = isbn;
		this.title = title;
	}

	// getters and setters omitted for brevity
}