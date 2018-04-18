package cn.alias.framework.websphere.jms.core;

public interface WebSphereMessageListener {

	/**
	 * @return 监听queue名称
	 */
	public String getTargetQueueName();
	
	/**
	 * 业务处理方法 
	 */
	public void handleMessage(Object messageData);
	
}
