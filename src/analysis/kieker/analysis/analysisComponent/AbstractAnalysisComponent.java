/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.analysisComponent;

import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the class {@link kieker.analysis.plugin.filter.AbstractFilterPlugin},
 * {@link kieker.analysis.plugin.reader.AbstractReaderPlugin} or {@link kieker.analysis.repository.AbstractRepository}. This is the base class for all other analysis
 * components within Kieker, like repositories or plugins. It should therefore <b>only</b> be used by the Kieker components itself as a base.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public abstract class AbstractAnalysisComponent implements IAnalysisComponent {

	/**
	 * The name of the property for the name. This should normally only be used by Kieker.
	 */
	public static final String CONFIG_NAME = "name-hiddenAndNeverExportedProperty";

	private static final Log LOG = LogFactory.getLog(AbstractAnalysisComponent.class);

	private static final AtomicInteger UNNAMED_COUNTER = new AtomicInteger(0);

	/**
	 * The project context of this component.
	 */
	// TODO #819 can be final in Kieker 1.8
	protected volatile IProjectContext projectContext;

	/**
	 * The current configuration of this component.
	 */
	protected final Configuration configuration;

	private final String name;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public AbstractAnalysisComponent(final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * Each AnalysisComponent requires a constructor with a Configuration object and a IProjectContext.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractAnalysisComponent(final Configuration configuration, final IProjectContext projectContext) {
		this.projectContext = projectContext;
		try {
			// somewhat dirty hack...
			configuration.setDefaultConfiguration(this.getDefaultConfiguration());
		} catch (final IllegalAccessException ex) {
			LOG.error("Unable to set repository default properties"); // ok to ignore ex here
		}
		this.configuration = configuration;

		// Try to determine the name
		String tmpName = configuration.getStringProperty(CONFIG_NAME);
		if (tmpName.length() == 0) {
			tmpName = this.getClass().getSimpleName() + '-' + UNNAMED_COUNTER.incrementAndGet();
		}
		this.name = tmpName;
	}

	/**
	 * This method delivers an instance of {@code Configuration} containing the default properties for this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * {@inheritDoc}
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * {@inheritDoc}
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Sets the project context atomically of this component to a new value. This property can only be set once. Every additional setting will be ignored but logged.
	 * <b>Do not call this method manually. A component will not be registered just by calling this method. Instead use the register methods of the
	 * {@link kieker.analysis.AnalysisController}. </b>
	 * 
	 * @param context
	 *            The new project context of this component.
	 * 
	 * @return true iff the project context of this plugin was not null and has been set to the given value.
	 * 
	 * @deprecated To be removed in 1.8
	 */
	@Deprecated
	public final boolean setProjectContext(final IProjectContext context) {
		synchronized (this) {
			if (this.projectContext == null) {
				this.projectContext = context;
				return true;
			} else if (this.projectContext == context) {
				return true;
			} else {
				LOG.warn("Project context of component already set to different project context.");
				return false;
			}
		}
	}
}
