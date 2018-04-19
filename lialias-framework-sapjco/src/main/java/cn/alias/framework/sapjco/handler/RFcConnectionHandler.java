package cn.alias.framework.sapjco.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;

public class RFcConnectionHandler implements JCoServerFunctionHandler {
	
	public static final String FUNCTION_CODE = "Z_SAMPLE_ABAP_CONNECTOR_CALL";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	RFcTIDHandler rFcTIDHandler = null;

	public RFcConnectionHandler() {
		super();
	}

	public RFcConnectionHandler(RFcTIDHandler rFcTIDHandler) {
		this.rFcTIDHandler = rFcTIDHandler;
	}

	private void printRequestInformation(JCoServerContext serverCtx, JCoFunction function) {
		logger.info("----------------------------------------------------------------");
		logger.info("call              : " + function.getName());
		logger.info("ConnectionId      : " + serverCtx.getConnectionID());
		logger.info("SessionId         : " + serverCtx.getSessionID());
		logger.info("TID               : " + serverCtx.getTID());
		logger.info("repository name   : " + serverCtx.getRepository().getName());
		logger.info("is in transaction : " + serverCtx.isInTransaction());
		logger.info("is stateful       : " + serverCtx.isStatefulSession());
		logger.info("----------------------------------------------------------------");
		logger.info("gwhost: " + serverCtx.getServer().getGatewayHost());
		logger.info("gwserv: " + serverCtx.getServer().getGatewayService());
		logger.info("progid: " + serverCtx.getServer().getProgramID());
		logger.info("----------------------------------------------------------------");
		logger.info("attributes  : ");
		logger.info(serverCtx.getConnectionAttributes().toString());
		logger.info("----------------------------------------------------------------");
		logger.info("req text: " + function.getImportParameterList().getString("REQUTEXT"));
	}

	public void handleRequest(JCoServerContext serverCtx, JCoFunction function) {
		// Check if the called function is the supported one.
		if (!function.getName().equals(FUNCTION_CODE)) {
			logger.error("Function '" + function.getName() + "' is no supported to be handled!");
			return;
		}
		
		printRequestInformation(serverCtx, function);

		function.getExportParameterList().setValue("ECHOTEXT", function.getImportParameterList().getString("REQUTEXT"));
		function.getExportParameterList().setValue("RESPTEXT", "Hello World");

		// In sample 3 (tRFC Server) we also set the status to executed:
		if (rFcTIDHandler != null)
			rFcTIDHandler.execute(serverCtx);
	}
}