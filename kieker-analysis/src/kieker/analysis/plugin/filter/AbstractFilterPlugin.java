/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.InvalidProjectContextException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every analysis plugin used within <i>Kieker</i>. For reader plugins, the class
 * {@link kieker.analysis.plugin.reader.AbstractReaderPlugin} should be used instead.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.5
 */
@Plugin
public abstract class AbstractFilterPlugin extends AbstractPlugin implements IFilterPlugin {

	/**
	 * Each Plugin requires a constructor with a Configuration object and a IProjectContext.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractFilterPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Register the filter
		if (projectContext instanceof AnalysisController) {
			((AnalysisController) projectContext).registerFilter(this);
		} else {
			throw new InvalidProjectContextException("Invalid analysis controller in constructor");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() { // NOPMD (default implementation)
		return true; // do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) { // NOPMD (default implementation)
		// do nothing
	}
}
