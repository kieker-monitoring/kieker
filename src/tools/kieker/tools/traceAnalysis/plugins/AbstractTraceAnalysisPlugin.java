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

package kieker.tools.traceAnalysis.plugins;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceAnalysisPlugin extends AbstractAnalysisPlugin {

	public static final String CONFIG_NAME = AbstractTraceAnalysisPlugin.class.getName() + ".name";
	private final String name;
	private final SystemModelRepository systemEntityFactory;

	public AbstractTraceAnalysisPlugin(final Configuration configuration, final AbstractRepository repositories[]) {
		super(configuration, repositories);

		/* Use the given repository if possible. */
		if ((repositories.length >= 1) && (repositories[0] instanceof SystemModelRepository)) {
			this.systemEntityFactory = (SystemModelRepository) repositories[0];
		} else {
			this.systemEntityFactory = null;
		}

		/* Try to load the name from the given configuration. */
		if (configuration.containsKey(AbstractTraceAnalysisPlugin.CONFIG_NAME)) {
			this.name = configuration.getStringProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME);
		} else {
			this.name = null;
		}
	}

	protected void printMessage(final String[] lines) {
		System.out.println("");
		System.out.println("#");
		System.out.println("# Plugin: " + this.name);
		for (final String l : lines) {
			System.out.println(l);
		}
	}

	protected final SystemModelRepository getSystemEntityFactory() {
		return this.systemEntityFactory;
	}

	@Override
	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[] { this.systemEntityFactory };
	}
}
