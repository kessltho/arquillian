package com.zuehlke.arquillian;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SomeBeanTest {

	@Inject
	SomeBean testee;

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(SomeBean.class, SomeVerySlowBean.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		// JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
		// .addClasses(SomeBean.class, SomeVerySlowBean.class, MyFastBean.class)
		// .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return jar;
	}

	@Test
	public void testDoSomethingMagic() {
		Assert.assertEquals("slow", testee.doSomethingMagic());
	}

}
