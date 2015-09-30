package com.zuehlke.arquillian;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Local(Greetable.class)
@Remote(GreetableRemote.class)
@Stateless(name = "greeter")
public class GreeterBean implements Greetable {

	@Override
	public Greeting greet(String name) {
		return new Greeting("Hello, " + name);
	}
}
