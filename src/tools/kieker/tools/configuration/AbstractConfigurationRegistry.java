package kieker.tools.configuration;

import java.util.concurrent.ConcurrentHashMap;

import kieker.common.configuration.Configuration;
import kieker.tools.configuration.exception.PluginNotFoundException;

/**
 * Abstract class for a ConfigurationRegistry.<br>
 * 
 * {@link AbstractUpdateableFilterPlugin} can register to a Registry and thereby be accessed and updated.
 * 
 * @author Markus Fischer
 * 
 */
public abstract class AbstractConfigurationRegistry implements IConfigurationRegistry {

	private final ConcurrentHashMap<String, AbstractUpdateableFilterPlugin> updateableFilters = new ConcurrentHashMap<String, AbstractUpdateableFilterPlugin>();

	/**
	 * Constructor.
	 */
	public AbstractConfigurationRegistry() {
		//
	}

	/**
	 * @see kieker.tools.configuration.IConfigurationRegistry#getUpdateableFilters()
	 * 
	 * @return Map containing all updateable filter plugins registered to this registry
	 */
	public ConcurrentHashMap<String, AbstractUpdateableFilterPlugin> getUpdateableFilters() {
		return this.updateableFilters;
	}

	/**
	 * @see kieker.tools.configuration.IConfigurationRegistry#registerUpdateableFilterPlugin(java.lang.String,
	 *      kieker.tools.configuration.AbstractUpdateableFilterPlugin)
	 * 
	 * @param id
	 *            id of the plugin
	 * @param plugin
	 *            plugin to register
	 */
	public void registerUpdateableFilterPlugin(final String id, final AbstractUpdateableFilterPlugin plugin) {
		this.getUpdateableFilters().put(id, plugin);
	}

	/**
	 * @throws kieker.tools.configuration.exception.PluginNotFoundException
	 * @see kieker.tools.configuration.IConfigurationRegistry#updateConfiguration(java.lang.String, kieker.common.configuration.Configuration, boolean)
	 * 
	 * @param configuration
	 *            Configuration containing the new values
	 * @param id
	 *            id of the plugin
	 * @param update
	 *            boolean value @see {@link AbstractUpdateableFilterPlugin#setCurrentConfiguration(Configuration, boolean)}
	 * 
	 * @throws PluginNotFoundException
	 *             Exception thrown if no plugin is found
	 */
	public void updateConfiguration(final String id, final Configuration configuration, final boolean update)
			throws PluginNotFoundException {
		final AbstractUpdateableFilterPlugin plugin = this.updateableFilters.get(id);
		if (plugin != null) {
			plugin.setCurrentConfiguration(configuration, update);
		} else {

			throw new PluginNotFoundException(id, configuration);
		}

	}
}
