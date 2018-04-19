package cn.alias.framework.sapjco.handler;

import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerContextInfo;
import com.sap.conn.jco.server.JCoServerErrorListener;
import com.sap.conn.jco.server.JCoServerExceptionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class RFcThrowableListener implements JCoServerErrorListener, JCoServerExceptionListener {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public void serverErrorOccurred(JCoServer jcoServer, String connectionId, JCoServerContextInfo serverCtx,
			Error error) {
		//System.out.println(">>> Error occured on " + jcoServer.getProgramID() + " connection " + connectionId);
		LOGGER.error(error.getMessage(),error);
	}

	public void serverExceptionOccurred(JCoServer jcoServer, String connectionId, JCoServerContextInfo serverCtx,
			Exception error) {
		//System.out.println(">>> Error occured on " + jcoServer.getProgramID() + " connection " + connectionId);
		LOGGER.error(error.getMessage(),error);
	}
}