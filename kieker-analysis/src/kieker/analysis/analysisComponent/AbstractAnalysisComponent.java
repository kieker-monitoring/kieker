/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.InvalidProjectContextException;
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

	/** The name of the property for the name. This should normally only be used by Kieker. */
	public static final String CONFIG_NAME = "name-hiddenAndNeverExportedProperty";

	protected static final Log LOG = LogFactory.getLog(AbstractAnalysisComponent.class); // NOPMD (logger for inheriting classes)

	private static final AtomicInteger UNNAMED_COUNTER = new AtomicInteger(0);

	/** The project context (usually the analysis controller) of this component. */
	protected final IProjectContext projectContext;

	/** The current configuration of this component. */
	protected final Configuration configuration;
	/** The log for this component. */
	protected final Log log; // NOPMD (logger for inheriting classes)

	/** The record time unit as provided by the project context. */
	protected final TimeUnit recordsTimeUnitFromProjectContext;

	private final String name;

	/**
	 * Each AnalysisComponent requires a constructor with a Configuration object and a IProjectContext.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 *
	 * @throws NullPointerException
	 *             If configuration or projectContext null
	 */
	public AbstractAnalysisComponent(final Configuration configuration, final IProjectContext projectContext) throws NullPointerException {
		if (null == projectContext) {
			throw new NullPointerException("Missing projectContext");
		}
		if (null == configuration) {
			throw new NullPointerException("Missing configuration");
		}
		this.projectContext = projectContext;
		// somewhat dirty hack...
		configuration.setDefaultConfiguration(this.getDefaultConfiguration());
		this.configuration = configuration;

		// Get the controller, as we have to register the name
		final AnalysisController ac;
		if (projectContext instanceof AnalysisController) {
			ac = (AnalysisController) projectContext;
		} else {
			throw new InvalidProjectContextException("Invalid analysis controller in constructor");
		}

		// Try to determine the name
		String tmpName = configuration.getStringProperty(CONFIG_NAME);
		while ((tmpName.length() == 0) || !ac.tryRegisterComponentName(tmpName)) {
			tmpName = this.getClass().getSimpleName() + '-' + UNNAMED_COUNTER.incrementAndGet();
		}
		this.name = tmpName;

		// As we have now a name, we can create our logger
		this.log = LogFactory.getLog(this.getClass().getName() + " (" + this.name + ")");

		// Try the record time unit
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			this.log.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.recordsTimeUnitFromProjectContext = recordTimeunit;
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
	@Override
	public abstract Configuration getCurrentConfiguration();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return this.name;
	}
}
