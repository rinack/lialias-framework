package cn.alias.framework.core;

import org.springframework.context.ApplicationContext;

public interface ApplicationStartupListener {

	 void useStartup(ApplicationContext applicationContext);
	 
}
