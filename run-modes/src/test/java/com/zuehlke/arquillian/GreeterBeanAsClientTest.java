package com.zuehlke.arquillian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GreeterBeanAsClientTest {

	@Inject
	GreeterBean greeter;

	@Deployment(testable = false)
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "greetable-test.jar")
				.addClasses(GreeterBean.class, Greetable.class, GreetableRemote.class,
						Greeting.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(jar.toString(true));

		jar.as(ZipExporter.class).exportTo(new File("target", "greetable-test.jar"), true);

		return jar;
	}

	@Test
	public void testGreetWithInjection() throws Exception {
		Greeting result = greeter.greet("arquillian");
		assertNotNull(result);
		assertEquals("Hello, arquillian", result.getText());
	}

	@Test
	public void testGreetWithLookup(@ArquillianResource URL deploymentUrl) throws Exception {
		Properties jndiProperties = createJndiProperties(deploymentUrl.getHost(), 4447);
		Context ctx = new InitialContext(jndiProperties);
		Object homeObject = ctx.lookup("greetable-test/greeter!" + GreetableRemote.class.getName());
		GreetableRemote g = (GreetableRemote) PortableRemoteObject.narrow(homeObject,
				GreetableRemote.class);
		Greeting result = g.greet("arquillian");
		assertNotNull(result);
		assertEquals("Hello, arquillian", result.getText());

	}

	private Properties createJndiProperties(String host, int port) {
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		String providerUrl = String.format("remote://%s:%d/", host, port);
		jndiProperties.put(Context.PROVIDER_URL, providerUrl);
		jndiProperties.put(Context.SECURITY_PRINCIPAL, "user");
		jndiProperties.put(Context.SECURITY_CREDENTIALS, "user.2000");
		jndiProperties.put("jboss.naming.client.ejb.context", true);
		return jndiProperties;
	}
}
