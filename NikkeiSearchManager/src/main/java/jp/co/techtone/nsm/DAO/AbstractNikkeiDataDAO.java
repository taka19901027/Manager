package jp.co.techtone.nsm.DAO;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public abstract class AbstractNikkeiDataDAO<T> implements NikkeiDataDAO<T> {
	public void init(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
}
