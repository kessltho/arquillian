package com.zuehlke.arquillian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Test;

public class MyBeanTest {

	@Test
	public void testEchoPerson() throws Exception {
		String host = "localhost";
		String port = "12377";
		String username = "MickeyMouse";
		String password = "top#secret!";

		Properties properties = new Properties();
		properties.put(Context.PROVIDER_URL, host + ":" + port);
		properties.put(Context.SECURITY_PRINCIPAL, username);
		properties.put(Context.SECURITY_CREDENTIALS, password);
		// many more properties

		InitialContext context = new InitialContext(properties);
		MyBean myBean = (MyBean) context.lookup("MyBean");

		assertNotNull(myBean);
		String result = myBean.echoPerson();
		assertEquals("Jack Sparrow", result);
	}
}
