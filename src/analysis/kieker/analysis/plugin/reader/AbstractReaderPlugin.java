/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.InvalidProjectContextException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every reader used within <i>Kieker</i>. For filter plugins, the class
 * {@link kieker.analysis.plugin.filter.AbstractFilterPlugin} should be used instead.
 * 
 * @author Nils Christian Ehmke
 */
@Plugin
public abstract class AbstractReaderPlugin extends AbstractPlugin implements IReaderPlugin {

	/**
	 * Each Plugin requires a constructor with a single Configuration object.
	 * 
	 * @param configuration
	 *            The configuration which should be used to initialize the object.
	 * @deprecated
	 */
	@Deprecated
	public AbstractReaderPlugin(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * The second "default constructor".
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 * 
	 * @since 1.7
	 */
	public AbstractReaderPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration);

		// Register the reader
		if (projectContext instanceof AnalysisController) {
			((AnalysisController) projectContext).registerReader(this);
		} else if (projectContext != null) {
			throw new InvalidProjectContextException("Invalid analysis controller in constructor");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#init()
	 */
	public boolean init() { // NOPMD (default implementation)
		return true;
	}
}
