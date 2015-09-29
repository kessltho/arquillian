package com.zuehlke.arquillian;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class StringJoinerTest {

	@Deployment
	public static Archive<?> createDeployment() throws IOException {
		WebArchive war = ShrinkWrap.create(WebArchive.class, "shrinkwrap-resolver-test.war")
				.addClass(StringJoiner.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsLibraries(resolveApacheCommonsLang3());

		System.out.println(war.toString(true));
		war.as(ZipExporter.class).exportTo(new File("target/shrinkwrap-resolver-test.war"), true);

		return war;
	}

	@Inject
	StringJoiner stringJoiner;

	@Test
	public void testJoinWithComma() {
		List<String> stringsToJoin = Arrays.asList("a", "b", "c");
		String result = stringJoiner.joinWithComma(stringsToJoin);
		assertEquals("a,b,c", result);
	}

	private static File[] resolveApacheCommonsLang3() {
		return Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile();
	}
}
