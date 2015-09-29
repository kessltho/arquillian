package com.zuehlke.arquillian;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.ejb.EJBAccessException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EchoServiceTest {

	@Inject
	private EchoService echoService;

	@Inject
	private RoleManager manager;

	@Inject
	private RoleUser user;

	@Deployment
	public static Archive<?> createDeployment() throws IOException {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "security-mini.jar")
				.addClasses(EchoService.class, RoleManager.class, RoleUser.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
		return jar;
	}

	@Test
	public void testPermitAll() throws Exception {
		String result = echoService.echo("papa smurf");
		assertEquals("hello, papa smurf", result);
	}

	@Test
	public void testAsManager() throws Exception {
		String callResult = manager.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				String result = echoService.echoManager("boss");
				return result;
			}
		});
		assertEquals("hello, manager boss", callResult);
	}

	@Test
	public void testAsUser() throws Exception {
		String callResult = user.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				String result = echoService.echoUser("gargamel");
				return result;
			}
		});
		assertEquals("hello, user gargamel", callResult);
	}

	@Test(expected = EJBAccessException.class)
	public void testUnauthorizedNoRole() throws Exception {
		echoService.echoManager("bigboss");
	}

	@Test(expected = EJBAccessException.class)
	public void testUnauthorizedAsUser() throws Exception {
		user.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				String result = echoService.echoManager("user");
				return result;
			}
		});
	}
}
