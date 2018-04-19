package cn.alias.framework.sapjco.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;


//@Singleton
//@Startup
public class HibersapBase {

	@PostConstruct
	public void rebindSessionManager() {

	}

	@PreDestroy
	public void unbindSessionManager() {

	}
}
