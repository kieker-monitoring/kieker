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

import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every plugin used within
 * <i>Kieker</i>. Every plugin should follow the following behavior:<br>
 * <ul>
 * <li>It should create and register its ports (input + output).
 * <li>The input ports should do whatever with the data and pass them to their output ports if necessary.
 * </ul>
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractAnalysisPlugin extends AbstractPlugin implements IAnalysisPlugin {

	public AbstractAnalysisPlugin(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void registerInputPort(final String name, final AbstractInputPort port) {
		super.registerInputPort(name, port);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void registerOutputPort(final String name, final OutputPort port) {
		super.registerOutputPort(name, port);
	}
}
