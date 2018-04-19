package cn.alias.framework.sapjco.jcoserver;

import java.util.Collection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.ServerDataProvider;

import cn.alias.framework.sapjco.configuration.HibersapConnectorBase;

@Configuration
public class JCoConnectorInitialize extends HibersapConnectorBase implements ApplicationContextAware, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${jco.server.gwhost:gmdev01}")
	private String gwhost;

	@Value("${jco.server.progid:SAP_TO_IWM_HZW}")
	private String progid;

	@Value("${jco.server.gwserv:sapgw00}")
	private String gwserv;

	@Value("${jco.server.repository_destination:ABAP_AS_WITHOUT_POOL}")
	private String repository_destination;

	@Value("${jco.server.connection_count:10}")
	private String connection_count;

	@Value("${jco.client.client:110}")
	private String client;

	@Value("${jco.client.user}")
	private String user;

	@Value("${jco.client.passwd}")
	private String passwd;

	@Value("${jco.client.lang:en}")
	private String lang;

	@Value("${jco.client.saprouter}")
	private String saprouter;

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

	@Value("${jco.rfc.client.hibersap:false}")
	private boolean hibersap;

	@Value("${jco.rfc.client.listener:false}")
	private boolean listener;

	private ApplicationContext applicationContext;

	/**
	 * JCO服务监听配置
	 * 
	 * @return
	 */
	@Bean
	public Properties servertProperties() {
		Properties servertProperties = new Properties();
		servertProperties.setProperty(ServerDataProvider.JCO_GWHOST, gwhost);
		servertProperties.setProperty(ServerDataProvider.JCO_GWSERV, gwserv);
		servertProperties.setProperty(ServerDataProvider.JCO_PROGID, progid);
		servertProperties.setProperty(ServerDataProvider.JCO_REP_DEST, repository_destination);
		servertProperties.setProperty(ServerDataProvider.JCO_CONNECTION_COUNT, connection_count);
		return servertProperties;
	}

	/**
	 * JCO client 配置
	 * 
	 * @return
	 */
	@Bean
	public Properties connectProperties() {
		Properties connectProperties = new Properties();
		connectProperties.setProperty(ServerDataProvider.JCO_REP_DEST, repository_destination);
		connectProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER, saprouter);
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, ashost);
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr);
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, user);
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, passwd);
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, lang);
		return connectProperties;
	}

	/**
	 * JCO 服务类
	 * 
	 * @return
	 */
	@Bean
	public RFcConnectorServer rfcConnectorServer() {
		return new RFcConnectorServer(servertProperties());
	}

	/**
	 * JCO 自定义连接实例
	 * 
	 * @return
	 */
	public JCoDestinationDataProvider jcoDestinationDataProvider() {
		Properties jcoProperties = connectProperties();
		return new JCoDestinationDataProvider(jcoProperties.getProperty(ServerDataProvider.JCO_REP_DEST),
				jcoProperties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (!listener) {
			logger.info("jco server 监听服务配置为:false 。。。");
			return;
		}

		if (hibersap) {
			dbContextSessionManager(repository_destination);
		} else {
			jcoDestinationDataProvider();
		}

		logger.info("正在准备启动  jco server 监听服务  。。。");

		// 获取 SAP function 组件
		Collection<AbstractJcoAbapCallHandler> abapCallHandlers = applicationContext
				.getBeansOfType(AbstractJcoAbapCallHandler.class).values();

		// 建立 jco 服务
		RFcConnectorServer rFcConnectorServer = rfcConnectorServer();
		rFcConnectorServer.addAbapCallHandlers(abapCallHandlers);

		try {

			rFcConnectorServer.listener();
			logger.info("jco server 监听服务启动完毕  。。。");

		} catch (Exception e) {
			logger.error("jco server 监听服务启动失败," + e.getMessage(), e);
		}

	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
