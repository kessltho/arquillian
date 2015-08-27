package org.arquillian.example;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ShrinkWrapSample {

	@Test
	public void createZip() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class,
				"myPackage.jar").addPackage(Basket.class.getPackage());
		System.out.println(archive.toString(true));
		File output = new File("target/test.zip");
		archive.as(ZipExporter.class).exportTo(output, true);
		System.out.println(output.getAbsolutePath());
	}

}
