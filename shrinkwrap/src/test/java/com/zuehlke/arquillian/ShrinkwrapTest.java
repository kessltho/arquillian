package com.zuehlke.arquillian;

import java.io.File;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

public class ShrinkwrapTest {

	@Test
	public void testShrinkwrap() {

		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb-jar.jar")
				.addClasses(UserService.class, User.class)
				.addPackage("com.zuehlke.arquillian.persistence")
				.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsManifestResource("test-persistence.xml", "persistence.xml");
		System.out.println(jar.toString(true) + "\n");

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")//
				.addClasses(SomeOtherClass.class);
		System.out.println(war.toString(true) + "\n");

		EnterpriseArchive ear = ShrinkWrap
				.create(EnterpriseArchive.class, "arquillian-testing-security.ear")
				.addAsModule(jar)//
				.addAsModule(war)//
				.setApplicationXML("test-application.xml")//
				.addAsManifestResource("jboss-ejb3.xml")//
				.addAsManifestResource("jbossas-ds.xml");
		System.out.println(ear.toString(true));

		ear.as(ZipExporter.class).exportTo(new File("target/shrinkwrap.ear.zip"), true);

	}
}
