package cn.alias.framework.websphere.jms.simple;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TextMessageListener implements MessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            String msgStr = msg.getText();
            //System.out.println("Receive message:" + msgStr);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}