package kieker.analysis.analysisComponent;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public abstract class AbstractAnalysisComponent {

	public static final String CONFIG_NAME = "name-hiddenAndNeverExportedProperty";

	private static final Log LOG = LogFactory.getLog(AbstractAnalysisComponent.class);

	protected volatile IProjectContext projectContext;
	protected final Configuration configuration;

	private final String name;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public AbstractAnalysisComponent(final Configuration configuration) {
		try {
			// TODO: somewhat dirty hack...
			configuration.setDefaultConfiguration(this.getDefaultConfiguration());
		} catch (final IllegalAccessException ex) {
			LOG.error("Unable to set repository default properties"); // ok to ignore ex here
		}
		this.configuration = configuration;

		// Try to determine the name
		this.name = configuration.getStringProperty(CONFIG_NAME);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will <b>not</b> be registered.
	 * 
	 * @since 1.7
	 */
	public AbstractAnalysisComponent(final Configuration configuration, final IProjectContext projectContext) {
		this(configuration);

		// Remember the project context. The registering will be done in the subclasses
		this.projectContext = projectContext;
	}

	/**
	 * This method delivers an instance of {@code Configuration} containing the default properties for this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * Delivers the current name of this component.
	 * 
	 * @return The name.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Sets the project context of this component to a new value. This property can only be set once. Every additional setting will be ignored but logged. <b>Do not
	 * call this method manually. A component will not be registered just by calling this method. Instead use the register methods of the {@link AnalysisController}.
	 * </b>
	 * 
	 * @param context
	 *            The new project context of this component.
	 * 
	 * @since 1.7
	 */
	// TODO Remove this method in 1.8 together with the register methods
	public final void setProjectContext(final IProjectContext context) {
		synchronized (this) {
			if (this.projectContext == null) {
				this.projectContext = context;
			} else {
				LOG.warn("Project context of component already set.");
			}
		}
	}
}
