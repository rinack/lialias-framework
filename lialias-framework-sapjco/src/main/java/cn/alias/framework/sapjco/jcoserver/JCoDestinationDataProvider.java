package cn.alias.framework.sapjco.jcoserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class JCoDestinationDataProvider implements DestinationDataProvider {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * From these properties all necessary destination data are gathered.
	 */
	private Map<String, Properties> providers = new HashMap<>();

	/**
	 * 连接事件监听
	 */
	private DestinationDataEventListener eL;

	/**
	 * Initializes this instance with the given {@code properties}. Performs a
	 * self-registration in case no instance of a {@link JCoDestinationDataProvider}
	 * is registered so far (see {@link #register(JCoDestinationDataProvider)}).
	 * 
	 * @param properties
	 *            the {@link #properties}
	 * 
	 */
	public JCoDestinationDataProvider(String destinationName, Properties properties) {
		super();
		
		if (destinationName == null)
			throw new NullPointerException("Please pass in the specified destinationName");
		
		providers.put(destinationName, properties);
		// Try to register this instance (in case there is not already another instance registered).
		register(this);
		
	}

	/**
	 * Flag that indicates if the method was already called.
	 */
	private static boolean registered = false;

	/**
	 * Registers the given {@code provider} as destination data provider at the
	 * {@link Environment}.
	 * 
	 * @param provider
	 *            the destination data provider to register
	 */
	private void register(JCoDestinationDataProvider provider) {
		// Check if a registration has already been performed.
		if (registered == false) {
			logger.info("There is no " + JCoDestinationDataProvider.class.getSimpleName()
					+ " registered so far. Registering a new instance.");
			// Register the destination data provider.
			Environment.registerDestinationDataProvider(provider);
			registered = true;
		}
	}

	@Override
	public Properties getDestinationProperties(String destinationName) {
		logger.info("Providing destination properties for destination '" + destinationName
				+ "' using the specified properties");
		if (destinationName == null)
			throw new NullPointerException("Please pass in the specified destinationName");
		if (providers.size() == 0)
			throw new IllegalStateException("The connection parameter attributes are empty to the provider");
		return providers.get(destinationName);
	}

	@Override
	public void setDestinationDataEventListener(DestinationDataEventListener listener) {
		this.eL = listener;
	}

	public void addDestinationProperties(String destinationName, Properties provider) {
		if (destinationName == null)
			throw new NullPointerException("Please pass in the specified destinationName");
		if (provider == null || provider.size() == 0)
			throw new IllegalStateException("The connection parameter attributes are empty to the provider");
		eL.updated(destinationName);
		providers.put(destinationName, provider);
	}

	@Override
	public boolean supportsEvents() {
		return false;
	}

}
