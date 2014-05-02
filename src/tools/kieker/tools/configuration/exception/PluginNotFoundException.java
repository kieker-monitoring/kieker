package kieker.tools.configuration.exception;

import kieker.common.configuration.Configuration;

/**
 * 
 * @author Markus
 * 
 * 
 *         The Plugin requested with the ID was not found in the registry.
 * 
 */
public class PluginNotFoundException extends Exception {

	private static final long serialVersionUID = -8803287298408230964L;
	private final String pluginID;
	private final Configuration configuration;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            requesting ID
	 * @param configuration
	 *            requesting configuration
	 */
	public PluginNotFoundException(final String id, final Configuration configuration) {
		this.pluginID = id;
		this.configuration = configuration;
	}

	/**
	 * @return the pluginID
	 */
	public String getPluginID() {
		return this.pluginID;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return this.configuration;
	}
}
