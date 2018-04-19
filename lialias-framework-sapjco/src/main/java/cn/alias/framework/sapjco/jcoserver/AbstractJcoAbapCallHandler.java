package cn.alias.framework.sapjco.jcoserver;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;

public abstract class AbstractJcoAbapCallHandler implements JCoServerFunctionHandler {

	private String rfcFunctionCode;

	private String rfcFunctionName;
	
	public AbstractJcoAbapCallHandler() {}

	public AbstractJcoAbapCallHandler(String jcoFunctionCode,String jcoFunctionName) {
		this.rfcFunctionCode = jcoFunctionCode;
		this.rfcFunctionName = jcoFunctionName;
	}
	
	public String getRfcFunctionCode() {
		return rfcFunctionCode;
	}

	public String getRfcFunctionName() {
		return rfcFunctionName;
	}

	@Override
	public abstract void handleRequest(JCoServerContext serverCtx, JCoFunction function);
	
}
