package com.zuehlke.arquillian;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class StringJoiner {

	public String joinWithComma(Collection<String> stringsToJoin) {
		return StringUtils.join(stringsToJoin, ",");
	}

}
