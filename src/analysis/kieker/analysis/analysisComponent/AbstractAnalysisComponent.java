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

	private static final AtomicInteger UNNAMED_COUNTER = new AtomicInteger(0);

	/**
	 * The project context of this component.
	 */
	protected final IProjectContext projectContext;

	/**
	 * The current configuration of this component.
	 */
	protected final Configuration configuration;

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
	public AbstractAnalysisComponent(final Configuration configuration, final IProjectContext projectContext) {
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
}
