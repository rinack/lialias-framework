package cn.alias.framework.sapjco.func;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.server.JCoServerContext;

import cn.alias.framework.sapjco.handler.RFcTIDHandler;
import cn.alias.framework.sapjco.jcoserver.AbstractJcoAbapCallHandler;

/**
 * 物料领料单下发 (物料预留)
 * 
 * @author Geely
 *
 */
@Component
public class JCoMaterialIssuedPullHandler extends AbstractJcoAbapCallHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String FUNCTION_CODE = "ZMM_01_S_RESB";

	public static final String FUNCTION_NAME = "物料领料单下发(预留单)";

	RFcTIDHandler rFcTIDHandler = null;

	public JCoMaterialIssuedPullHandler() {
		super(FUNCTION_CODE, FUNCTION_NAME);
	}

	public JCoMaterialIssuedPullHandler(RFcTIDHandler rFcTIDHandler) {
		this.rFcTIDHandler = rFcTIDHandler;
	}

	@Override
	public void handleRequest(JCoServerContext serverCtx, JCoFunction function) {

		// Check if the called function is the supported one.
		if (!function.getName().equals(FUNCTION_CODE)) {
			logger.error("Function '" + function.getName() + "' is no supported to be handled!");
			return;
		}

		// 打印
		printRequestInformation(serverCtx, function);

		// 获取参数表
		JCoParameterList tableParams = function.getTableParameterList();

		// 设置返回结果
		JCoTable options = tableParams.getTable("ZMESG");

		try {

			String rsnum = function.getImportParameterList().getString("RSNUM");

			// 获取输入参数
			JCoTable jcoTable = tableParams.getTable("I_INPUT");

			for (int i = 0; i < jcoTable.getNumRows(); i++) {
				jcoTable.setRow(i);

				String aufnr = jcoTable.getString("AUFNR");
				String lgort = jcoTable.getString("LGORT");

				System.out.println("AUFNR:" + aufnr + " LGORT:" + lgort);

			}

			// 设置导出参数
			function.getExportParameterList().setValue("RES_PAYLOAD", rsnum);

			// 设置返回结果
			options.appendRow();
			options.setValue("RSNUM", rsnum);
			options.setValue("TYPE", "S");
			options.setValue("MESSAGE", "成功");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// In sample 3 (tRFC Server) we also set the status to executed:
		if (rFcTIDHandler != null)
			rFcTIDHandler.execute(serverCtx);

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
	}

}