package com.zuehlke.arquillian;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
public class EchoService {

	@PermitAll
	public String echo(String name) {
		return "hello, " + name;
	}

	@RolesAllowed({ "Manager" })
	public String echoManager(String name) {
		return "hello, manager " + name;
	}

	@RolesAllowed({ "User" })
	public String echoUser(String name) {
		return "hello, user " + name;
	}

}
