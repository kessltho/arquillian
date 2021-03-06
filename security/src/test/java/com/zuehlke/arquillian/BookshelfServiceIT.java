package com.zuehlke.arquillian;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ejb.EJBAccessException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BookshelfServiceIT {

	@Inject
	private BookshelfService bookshelfService;

	@Inject
	private BookshelfManager manager;

	@Inject
	private BookshelfUser user;

	@Deployment
	public static Archive<?> createDeployment() throws IOException {

		File[] libraries = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("org.apache.commons:commons-math3").withTransitivity().asFile();

		JavaArchive main = ShrinkWrap
				.create(JavaArchive.class, "ejb-jar.jar")
				.addClasses(Book.class, BookshelfService.class, BookshelfManager.class,
						BookshelfUser.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsManifestResource("test-persistence.xml", "persistence.xml");

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war").addClasses(
				BookshelfServiceIT.class);

		EnterpriseArchive ear = ShrinkWrap
				.create(EnterpriseArchive.class, "arquillian-testing-security.ear")

				.addAsManifestResource("jbossas-ds.xml").addAsModule(main).addAsModule(war)
				.addAsLibraries(libraries).setApplicationXML("test-application.xml")

				.addAsManifestResource("jboss-ejb3.xml").addAsManifestResource("jbossas-ds.xml");

		System.out.println(ear.toString(true));
		ear.as(ZipExporter.class).exportTo(new File("C:/temp/deployment.ear"), true);

		return ear;
	}

	@InSequence(2)
	@Test
	public void testAsManager() throws Exception {
		manager.call(new Callable<Book>() {

			@Override
			public Book call() throws Exception {
				bookshelfService.addBook(new Book("978-1-4302-4626-8", "Beginning Java EE 7"));
				bookshelfService.addBook(new Book("978-1-4493-2829-0",
						"Continuous Enterprise Development in Java"));

				List<Book> books = bookshelfService.getBooks();
				Assert.assertEquals("List.size()", 2, books.size());

				for (Book book : books) {
					bookshelfService.deleteBook(book);
				}

				Assert.assertEquals("BookshelfService.getBooks()", 0, bookshelfService.getBooks()
						.size());
				return null;
			}
		});
	}

	@InSequence(3)
	@Test
	public void testAsUser() throws Exception {
		user.call(new Callable<Book>() {

			@Override
			public Book call() throws Exception {
				bookshelfService.addBook(new Book("978-1-4302-4626-8", "Beginning Java EE 7"));
				bookshelfService.addBook(new Book("978-1-4493-2829-0",
						"Continuous Enterprise Development in Java"));

				List<Book> books = bookshelfService.getBooks();
				Assert.assertEquals("List.size()", 2, books.size());

				for (Book book : books) {
					try {
						bookshelfService.deleteBook(book);
						Assert.fail("Users should not be allowed to delete");
					} catch (EJBAccessException e) {
						// Good, users cannot delete things
					}
				}

				// The list should not be empty
				Assert.assertEquals("BookshelfService.getBooks()", 2, bookshelfService.getBooks()
						.size());
				return null;
			}
		});
	}

	@InSequence(1)
	@Test
	public void testUnauthenticated() throws Exception {
		try {
			bookshelfService.addBook(new Book("978-1-4302-4626-8", "Beginning Java EE 7"));
			Assert.fail("Unauthenticated users should not be able to add books");
		} catch (EJBAccessException e) {
			// Good, unauthenticated users cannot add things
		}

		try {
			bookshelfService.deleteBook(null);
			Assert.fail("Unauthenticated users should not be allowed to delete");
		} catch (EJBAccessException e) {
			// Good, unauthenticated users cannot delete things
		}

		try {
			// Read access should be allowed
			List<Book> books = bookshelfService.getBooks();
			Assert.assertEquals("BookshelfService.getBooks()", 0, books.size());
		} catch (EJBAccessException e) {
			Assert.fail("Read access should be allowed");
		}
	}
}
