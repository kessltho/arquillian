package com.zuehlke.arquillian;

import javax.inject.Inject;

public class SomeBean {

	@Inject
	SomeVerySlowBean otherBean;

	public String doSomethingMagic() {
		return otherBean.someSlowOperation();
	}

}
