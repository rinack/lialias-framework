package cn.alias.framework.websphere.jms.core;

import java.util.Collection;
import java.util.Iterator;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import com.ibm.mq.jms.MQQueueConnectionFactory;

@Configuration
public class WebSphereMQConfiguration implements ApplicationContextAware, InitializingBean {

	ApplicationContext applicationContext;

	@Value("${ibm.mq.client.hostname:127.0.0.1}")
	private String hostname;

	@Value("${ibm.mq.client.ccsid:1381}")
	private int ccsid;

	@Value("${ibm.mq.client.queuemanagername:QM}")
	private String queueManagerName;

	@Value("${ibm.mq.client.port:1414}")
	private int port;

	@Value("${ibm.mq.client.channelName:QM_ACK}")
	private String channelName;

	@Bean
	public ConnectionFactory connectionFactory() throws JMSException {
		MQQueueConnectionFactory mcf = new MQQueueConnectionFactory();
		mcf.setHostName(hostname);
		mcf.setQueueManager(queueManagerName);
		mcf.setCCSID(ccsid);
		mcf.setChannel(channelName);
		mcf.setPort(port);
		return mcf;
	}

	@Bean
	@Autowired
	public CachingConnectionFactory rabbitTemplate(ConnectionFactory cf) {
		return new CachingConnectionFactory(cf);
	}

	@Bean
	@Autowired
	public WebSphereMQService webSphereMQService(ConnectionFactory connectionFactory) {
		return new SimpleWebSphereMQService(connectionFactory);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<AbstractWebSphereMessageListener> webSphereListeners = applicationContext.getBeansOfType(AbstractWebSphereMessageListener.class).values();
		WebSphereMQService webSphereMQService = applicationContext.getBean(WebSphereMQService.class);
		Iterator<AbstractWebSphereMessageListener> it = webSphereListeners.iterator();
		while (it.hasNext()) {
			webSphereMQService.listen(it.next());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
	}

}
