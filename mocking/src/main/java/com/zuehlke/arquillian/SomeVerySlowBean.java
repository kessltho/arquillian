package com.zuehlke.arquillian;

public class SomeVerySlowBean {

	public String someSlowOperation() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "slow";
	}
}
