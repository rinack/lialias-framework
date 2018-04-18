package cn.alias.framework.websphere.jms.core;

import javax.jms.JMSException;

public interface WebSphereMQService {

	/**
	 * 注册消费者
	 *
	 * @param l 消费者实例
	 * @throws JMSException 
     */
	void listen(AbstractWebSphereMessageListener l) throws JMSException;
	
}
