package com.zuehlke.arquillian;

import javax.inject.Inject;

public class Greeter {

	@Inject
	private PhraseBuilder phraseBuilder;

	public String createGreeting(String name) {
		return phraseBuilder.buildPhrase(name);
	}
}
