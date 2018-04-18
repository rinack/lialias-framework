package cn.alias.framework.websphere.jms.core;

import javax.jms.Message;

public abstract class AbstractWebSphereMessageListener {

	private String targetQueueName;

	public AbstractWebSphereMessageListener(String queueName) {
		this.targetQueueName = queueName;
	}

	public void setTargetQueueName(String queueName) {
		this.targetQueueName = queueName;
	}

	public String getTargetQueueName() {
		return targetQueueName;
	}

	public abstract void onMessage(Message message);

}
