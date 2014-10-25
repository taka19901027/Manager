package jp.co.techtone.nsm.controller;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public abstract class AbstractNsmController {

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
}
