package kieker.tools.configuration;

import java.util.Map;

import kieker.common.configuration.Configuration;
import kieker.tools.configuration.exception.PluginNotFoundException;

/**
 * 
 * @author Markus Fischer
 * 
 *         Registry for updateable filters
 */
public interface IConfigurationRegistry {

	/**
	 * 
	 * @return a map containing all updateable FilterPlugins.
	 */
	public abstract Map<String, AbstractUpdateableFilterPlugin> getUpdateableFilters();

	/**
	 * Register an AbstractUpdateableFilterPlugin to the registry.
	 * 
	 * @param id
	 *            Unique id for the plugin (within the scope of a registry)
	 * @param plugin
	 *            plugin to be registered
	 */
	public abstract void registerUpdateableFilterPlugin(String id,
			AbstractUpdateableFilterPlugin plugin);

	/**
	 * Update the configuration of a FilterPlugin identified by its id.
	 * 
	 * @param id
	 *            id of the plugin to be updated
	 * @param configuration
	 *            Configuration containing the new values
	 * @param update
	 *            boolean value @see {@link AbstractUpdateableFilterPlugin#setCurrentConfiguration(Configuration, boolean)}
	 * 
	 * @throws PluginNotFoundException
	 *             plugin was not found
	 */
	public abstract void updateConfiguration(String id,
			Configuration configuration, boolean update) throws PluginNotFoundException;

}
