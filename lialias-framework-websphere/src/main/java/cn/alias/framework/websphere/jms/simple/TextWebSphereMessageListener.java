package cn.alias.framework.websphere.jms.simple;

import javax.jms.Message;

import org.springframework.stereotype.Component;

import cn.alias.framework.websphere.jms.core.AbstractWebSphereMessageListener;

@Component
public class TextWebSphereMessageListener extends AbstractWebSphereMessageListener {

	public TextWebSphereMessageListener() {
		super("QUEUE_RECV");
	}

	@Override
	public void onMessage(Message arg0) {
		//System.out.println("----------------------------------------");
		//System.out.println(arg0);
		//System.out.println("----------------------------------------");
	}

}
