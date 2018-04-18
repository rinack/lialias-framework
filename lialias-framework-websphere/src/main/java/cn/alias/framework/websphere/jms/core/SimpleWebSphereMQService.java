package cn.alias.framework.websphere.jms.core;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConverter;

import com.ibm.mq.jms.MQQueue;

public class SimpleWebSphereMQService implements WebSphereMQService {

	private static final Logger logger = LoggerFactory.getLogger(SimpleWebSphereMQService.class);

	private ConnectionFactory connectionFactory;

	private MessageConverter messageConverter;

	public SimpleWebSphereMQService(ConnectionFactory connectionFactory2) {
		this.connectionFactory = connectionFactory2;
	}

	@Override
	public void listen(final AbstractWebSphereMessageListener listener) throws JMSException {
		this.listen(listener, null);
	}

	public void listen(final AbstractWebSphereMessageListener listener, Integer concurrentConsumers)
			throws JMSException {
		
		/**
		MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
			@SuppressWarnings("unused")
			void handleMessage(String text) {
				System.out.println(text);
			}
		});
		**/
		
		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					listener.onMessage(message);
				} catch (Exception e) {
					logger.error("MQ listener handle method exception " + e.getMessage(), e);
				}
			}
		});

		if (messageConverter != null) {
			adapter.setMessageConverter(messageConverter);
		}

		MQQueue mqQueue = new MQQueue(listener.getTargetQueueName());
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(new CachingConnectionFactory(connectionFactory));
		container.setDestination(mqQueue);
		container.setMessageListener(adapter);
		// container.setMessageListener(listener);

		if (concurrentConsumers != null && concurrentConsumers.intValue() > 0) {
			container.setConcurrentConsumers(concurrentConsumers);
		}

		container.initialize();
		container.start();

	}

}
