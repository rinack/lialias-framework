package cn.alias.framework.sapjco.jcoserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.ext.Environment;
import com.sap.conn.jco.ext.ServerDataEventListener;
import com.sap.conn.jco.ext.ServerDataProvider;

public class JCoServerDataProvider implements ServerDataProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * From these properties all necessary destination data are gathered.
	 */
	private Map<String, Properties> providers = new HashMap<>();

	/**
	 * 连接事件监听
	 */
	private ServerDataEventListener eL;

	/**
	 * Initializes this instance with the given {@code properties}. Performs a
	 * self-registration in case no instance of a {@link JCoServerDataProvider} is
	 * registered so far (see {@link #register(JCoServerDataProvider)}).
	 * 
	 * @param properties
	 *            the {@link #properties}
	 * 
	 */
	public JCoServerDataProvider(String destinationName, Properties properties) {
		super();
		providers.put(destinationName, properties);
		// Try to register this instance (in case there is not already another
		// instance registered).
		register(this);
	}

	/**
	 * Flag that indicates if the method was already called.
	 */
	private static boolean registered = false;

	/**
	 * Registers the given {@code provider} as server data provider at the
	 * {@link Environment}.
	 * 
	 * @param provider
	 *            the server data provider to register
	 */
	private void register(JCoServerDataProvider provider) {
		// Check if a registration has already been performed.
		if (registered == false) {
			logger.info("There is no " + JCoServerDataProvider.class.getSimpleName()
					+ " registered so far. Registering a new instance.");
			// Register the destination data provider.
			Environment.registerServerDataProvider(provider);
			registered = true;
		}
	}

	@Override
	public Properties getServerProperties(String serverName) {
		logger.info("Providing server properties for server '" + serverName + "' using the specified properties");
		if (serverName == null)
			throw new NullPointerException("Please pass in the specified serverName");
		if (providers.size() == 0)
			throw new IllegalStateException("The server parameter attributes are empty to the provider");
		return providers.get(serverName);
	}

	@Override
	public void setServerDataEventListener(ServerDataEventListener listener) {
		this.eL = listener;
	}

	public void addServerProperties(String serverName, Properties provider) {
		if (serverName == null)
			throw new NullPointerException("Please pass in the specified destinationName");
		if (provider == null || provider.size() == 0)
			throw new IllegalStateException("The connection parameter attributes are empty to the provider");
		eL.updated(serverName);
		providers.put(serverName, provider);
	}

	@Override
	public boolean supportsEvents() {
		return false;
	}

}
