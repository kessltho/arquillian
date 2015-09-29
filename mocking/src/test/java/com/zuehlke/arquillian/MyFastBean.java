package com.zuehlke.arquillian;

import javax.enterprise.inject.Specializes;

@Specializes
public class MyFastBean extends SomeVerySlowBean {

	@Override
	public String someSlowOperation() {
		return "fast";
	}
}
