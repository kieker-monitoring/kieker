package kieker.tools.configuration;


/**
 * 
 * @author Markus Fischer
 * 
 *         Singleton to provide a global registry for all updateable Filters.<br>
 *         All updateable Filters have to be stored to the Registry with a unique ID.
 */
public final class GlobalConfigurationRegistry extends AbstractConfigurationRegistry {

	private static IConfigurationRegistry instance = new GlobalConfigurationRegistry();

	private GlobalConfigurationRegistry() {}

	public static IConfigurationRegistry getInstance() {
		return instance;
	}
}
