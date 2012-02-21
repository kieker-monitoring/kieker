/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.plugin;

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every analysis plugin used within <i>Kieker</i>. For reader plugins, the class {@link AbstractReaderPlugin} should be used
 * instead.
 * 
 * @author Nils Christian Ehmke
 */
@Plugin
public abstract class AbstractAnalysisPlugin extends AbstractPlugin implements IAnalysisPlugin {

	/**
	 * The constructor for the plugin. Every plugin must have this constructor.
	 * 
	 * @param configuration
	 *            The configuration to use for this plugin.
	 */
	public AbstractAnalysisPlugin(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public boolean init() {
		return true; // do nothing
	}

	@Override
	public void terminate(final boolean error) {
		// do nothing
	}
}
