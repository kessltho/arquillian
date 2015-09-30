package com.zuehlke.arquillian;


// implements Serializable
public class Greeting {

	private final String text;

	public Greeting(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
