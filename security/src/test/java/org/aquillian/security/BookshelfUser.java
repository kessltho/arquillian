package org.aquillian.security;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless
@RunAs("User")
@PermitAll
public class BookshelfUser {
	public <V> V call(Callable<V> callable) throws Exception {
		return callable.call();
	}
}