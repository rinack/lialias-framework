package cn.alias.framework.websphere.jms.simple;

import javax.jms.Message;

import org.springframework.stereotype.Component;

import cn.alias.framework.websphere.jms.core.AbstractWebSphereMessageListener;

@Component
public class MQWebSphereMessageListener extends AbstractWebSphereMessageListener {

	public MQWebSphereMessageListener() {
		super("T");
	}

	@Override
	public void onMessage(Message arg0) {
		//System.out.println("*****************************************");
		//System.out.println(arg0);
		//System.out.println("*****************************************");
	}

}
