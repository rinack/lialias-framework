package cn.alias.framework.sapjco.configuration;

import java.util.HashMap;
import java.util.Map;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class HibersapConnectorBase {

	@Value("${jco.client.client:110}")
	private String client;

	@Value("${jco.client.user}")
	private String user;

	@Value("${jco.client.passwd}")
	private String passwd;

	@Value("${jco.client.lang:en}")
	private String lang;

	@Value("${jco.client.ashost}")
	private String ashost;

	@Value("${jco.client.sysnr:00}")
	private String sysnr;

	@Value("${jco.destination.pool_capacity:20}")
	private String pool_capacity;

	@Value("${jco.destination.peak_limit:30}")
	private String peak_limit;

	@Value("${jco.destination.max_get_client_time:3000}")
	private String max_get_client_time;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 连接配置信息集合
	 */
	private static Map<String, SessionManager> SESSION_MANAGER_MAP = new HashMap<>();

	/**
	 * 
	 * 获取或者新建指定名称的会话连接
	 * 
	 * 线程安全
	 * 
	 * @param sessionManagerName
	 * @return
	 */
	protected SessionManager dbContextSessionManager(String sessionManagerName) {
		synchronized (SESSION_MANAGER_MAP) {
			if (!SESSION_MANAGER_MAP.containsKey(sessionManagerName)) {
				SESSION_MANAGER_MAP.put(sessionManagerName, this.createSessionManager(sessionManagerName));
			}
			return SESSION_MANAGER_MAP.get(sessionManagerName);
		}
	}
	
	/**
	 * 
	 * 获取或者新建指定名称的会话连接
	 * 
	 * 线程安全
	 * 
	 * @param sessionManagerName
	 * @return
	 */
	protected SessionManager dbContextSessionManager(String sessionManagerName, final Class<?>... bapiClasses) {
		synchronized (SESSION_MANAGER_MAP) {
			if (!SESSION_MANAGER_MAP.containsKey(sessionManagerName)) {
				SESSION_MANAGER_MAP.put(sessionManagerName, this.createSessionManager(sessionManagerName,bapiClasses));
			}
			return SESSION_MANAGER_MAP.get(sessionManagerName);
		}
	}

	/**
	 * 
	 * 读取 META-INF/hibersap.xml 加载配置文件及 BAPI类
	 * 
	 * 非线程安全
	 * 
	 * @return
	 */
	private synchronized SessionManager createSessionManager(String sessionManagerName) {
		try {

			AnnotationConfiguration configuration = new AnnotationConfiguration(sessionManagerName);
			configuration.buildSessionManager().getConfig().setProperty("jco.client.client", client)
					.setContext(org.hibersap.execution.jco.JCoContext.class.getName())
					.setProperty("jco.client.user", user).setProperty("jco.client.passwd", passwd)
					.setProperty("jco.client.lang", lang).setProperty("jco.client.ashost", ashost)
					.setProperty("jco.client.sysnr", sysnr).setProperty("jco.destination.peak_limit", peak_limit)
					.setProperty("jco.destination.pool_capacity", pool_capacity)
					.setProperty("jco.destination.max_get_client_time", max_get_client_time);
			// 属性更改后重新编译更新配置
			return configuration.buildSessionManager();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * 传入BAPI的方式创建连接
	 * 
	 * @param bapiClasses
	 * @return
	 */
	protected SessionManager createSessionManager(String sessionManagerName, final Class<?>... bapiClasses) {
		try {

			SessionManagerConfig cfg = new SessionManagerConfig(sessionManagerName)
					.setContext(org.hibersap.execution.jco.JCoContext.class.getName())
					.setProperty("jco.client.client", client).setProperty("jco.client.user", user)
					.setProperty("jco.client.passwd", passwd).setProperty("jco.client.lang", lang)
					.setProperty("jco.client.ashost", ashost).setProperty("jco.client.sysnr", sysnr)
					.setProperty("jco.destination.peak_limit", peak_limit)
					.setProperty("jco.destination.pool_capacity", pool_capacity)
					.setProperty("jco.destination.max_get_client_time", max_get_client_time);
			AnnotationConfiguration configuration = new AnnotationConfiguration(cfg);
			configuration.addBapiClasses(bapiClasses);

			return configuration.buildSessionManager();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

}

