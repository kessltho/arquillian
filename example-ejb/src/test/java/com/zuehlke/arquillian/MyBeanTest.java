package com.zuehlke.arquillian;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

public class MyBeanTest {

	public void testLookup() throws Exception {
		String host = "localhost";
		String port = "12377";

		Properties properties = new Properties();
		properties.put(Context.PROVIDER_URL, host + ":" + port);

		InitialContext context = new InitialContext(properties);
		MyBean myBean = (MyBean) context.lookup("MyBean");

		assertNotNull(myBean);
		String result = myBean.echoPerson();
		assertEquals("Jack Sparrow", result);
	}
}
