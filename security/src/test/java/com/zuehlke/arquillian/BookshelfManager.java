package com.zuehlke.arquillian;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

/**
 * Allows our test code to execute in the desired security scope.
 */
@Stateless
@RunAs("Manager")
@PermitAll
public class BookshelfManager {

	public <V> V call(Callable<V> callable) throws Exception {
		return callable.call();
	}
}
