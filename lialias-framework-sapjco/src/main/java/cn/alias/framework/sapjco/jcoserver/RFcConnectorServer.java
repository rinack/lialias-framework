package cn.alias.framework.sapjco.jcoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.server.DefaultServerHandlerFactory;
import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerFactory;
import com.sap.conn.jco.server.JCoServerFunctionHandler;

import cn.alias.framework.sapjco.handler.RFcConnectionHandler;
import cn.alias.framework.sapjco.handler.RFcStateChangedListener;
import cn.alias.framework.sapjco.handler.RFcTIDHandler;
import cn.alias.framework.sapjco.handler.RFcThrowableListener;

/**
 * 因 hibersap 框架内部已经有此实现 所以 当项目中有使用 hibersap框架的时候此行 JCoDestinationDataProvider
 * 不需要 需注释 new
 * JCoDestinationDataProvider(jcoProperties.getProperty(ServerDataProvider.JCO_REP_DEST),
 * jcoProperties);
 * 
 * @author Geely
 *
 */
@Component
public class RFcConnectorServer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Collection<AbstractJcoAbapCallHandler> abapCallHandlers = new HashSet<>();

	/**
	 * The properties necessary to define the server and destination.
	 */
	private Properties properties;

	/**
	 * 获取 JCO连接信息
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * jco server
	 */
	private JCoServer server;

	public RFcConnectorServer() {
	}

	/**
	 * 传入配置连接属性对象
	 * 
	 * @param jcoProperties
	 * @throws IOException
	 */
	public RFcConnectorServer(Properties jcoProperties) {
		this.properties = jcoProperties;
		/**
		 * 因 hibersap 框架内部已经有此实现 所以 当项目中有使用 hibersap框架的时候次行需注释 new
		 * JCoDestinationDataProvider(jcoProperties.getProperty(ServerDataProvider.JCO_REP_DEST),
		 * jcoProperties);
		 */

		new JCoServerDataProvider(jcoProperties.getProperty(ServerDataProvider.JCO_PROGID), jcoProperties);

	}

	/**
	 * Runnable to listen to the standard input stream to end the server.
	 */
	private Runnable stdInListener = new Runnable() {
		@Override
		public void run() {

			InputStream in = System.in;
			try {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String line = null;

				while ((line = br.readLine()) != null) {
					// Check if the server should be ended.
					if ("end".equalsIgnoreCase(line)) {
						// Stop the server.
						server.stop();
					}
				}
				
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			
		}
	};

	/**
	 * 注册添加 JCO 函数
	 * 
	 * @param abapCallHandlers
	 */
	public void addAbapCallHandlers(Collection<AbstractJcoAbapCallHandler> abapCallHandlers) {
		this.abapCallHandlers.addAll(abapCallHandlers);
	}

	/**
	 * 启动监听
	 */
	public void listener() {

		try {
			server = JCoServerFactory.getServer(properties.getProperty(ServerDataProvider.JCO_PROGID));
		} catch (JCoException e) {
			throw new RuntimeException("Unable to create the server "
					+ properties.getProperty(ServerDataProvider.JCO_PROGID) + ", because of " + e.getMessage(), e);
		}

		// 创建 FunctionHandlerFactory 工厂
		DefaultServerHandlerFactory.FunctionHandlerFactory factory = new DefaultServerHandlerFactory.FunctionHandlerFactory();
		JCoServerFunctionHandler abapCallHandler = new RFcConnectionHandler();
		// 消息监听模块示例
		factory.registerHandler(RFcConnectionHandler.FUNCTION_CODE, abapCallHandler);

		for (AbstractJcoAbapCallHandler abstractJcoAbapCallHandler : abapCallHandlers) {
			// 注册模块函数
			factory.registerHandler(abstractJcoAbapCallHandler.getRfcFunctionCode(), abstractJcoAbapCallHandler);
		}

		server.setCallHandlerFactory(factory);

		// additionally to step 1
		RFcThrowableListener errorListener = new RFcThrowableListener();
		// Add listener for errors.
		server.addServerErrorListener(errorListener);
		// Add listener for exceptions.
		server.addServerExceptionListener(errorListener);
		// Add server state change listener.
		RFcStateChangedListener statelistener = new RFcStateChangedListener();
		server.addServerStateChangedListener(statelistener);

		RFcTIDHandler rFcTIDHandler = new RFcTIDHandler();
		server.setTIDHandler(rFcTIDHandler);

		// Add a stdIn listener.
		// new Thread(stdInListener).start();

		// Start the server
		server.start();

		logger.info("The program can be stopped typing 'END'");

	}

}