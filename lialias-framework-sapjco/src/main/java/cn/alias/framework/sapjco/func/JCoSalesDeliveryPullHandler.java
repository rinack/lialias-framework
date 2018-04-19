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
 * 销售交货单下发
 * 
 * @author Geely
 *
 */
@Component
public class JCoSalesDeliveryPullHandler extends AbstractJcoAbapCallHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String FUNCTION_CODE = "ZSD_02_SO_POST";

	public static final String FUNCTION_NAME = "销售交货单下发";

	RFcTIDHandler rFcTIDHandler = null;

	public JCoSalesDeliveryPullHandler() {
		super(FUNCTION_CODE, FUNCTION_NAME);
	}

	public JCoSalesDeliveryPullHandler(RFcTIDHandler rFcTIDHandler) {
		this.rFcTIDHandler = rFcTIDHandler;
	}

	@Override
	public void handleRequest(JCoServerContext serverCtx, JCoFunction function) {

		// Check if the called function is the supported one.
		if (!function.getName().equals(FUNCTION_CODE)) {
			logger.error("Function '" + function.getName() + "' is no supported to be handled!");
			return;
		}

		// 获取参数表
		JCoParameterList tableParams = function.getTableParameterList();

		// 设置返回结果
		JCoTable options = tableParams.getTable("ZMESG");

		try {

			// 获取交货单上报结构header
			JCoTable jcoTable = tableParams.getTable("ZPO_HEADER");
			for (int i = 0; i < jcoTable.getNumRows(); i++) {
				jcoTable.setRow(i);

				String vbeln = jcoTable.getString("VBELN");
				String vstel = jcoTable.getString("VSTEL");

				System.out.println("VBELN:" + vbeln + " VSTEL:" + vstel);

			}

			String type = "E";
			String msg = "失败";

			for (int i = 0; i < jcoTable.getNumRows(); i++) {
				options.appendRow();
				options.setValue("MESSAGE_V1", i);
				options.setValue("TYPE", type);
				options.setValue("MESSAGE", msg);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// In sample 3 (tRFC Server) we also set the status to executed:
		if (rFcTIDHandler != null)
			rFcTIDHandler.execute(serverCtx);

	}
}