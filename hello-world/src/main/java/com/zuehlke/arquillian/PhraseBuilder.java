package com.zuehlke.arquillian;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

public class PhraseBuilder {

	private String greetingPattern;

	public String buildPhrase(String name) {
		return MessageFormat.format(greetingPattern, name);
	}

	@PostConstruct
	public void initialize() {
		greetingPattern = "Hi, {0}!";
	}
}
