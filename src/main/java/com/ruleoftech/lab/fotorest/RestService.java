package com.ruleoftech.lab.fotorest;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class RestService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestService.class);

	public String search(String criteriaAsGET) {
		LOGGER.trace("{'method':'search', 'params':{'" + criteriaAsGET + "'}}");

		return "foo";
	}

}
