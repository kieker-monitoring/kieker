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

package livedemo.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;

import livedemo.entities.Record;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@Plugin(programmaticOnly = true,
		description = "A filter collecting incoming objects in a list",
		outputPorts = @OutputPort(
				name = OER2RecordFilter.OUTPUT_PORT_NAME,
				eventTypes = { Record.class },
				description = "Provides each incoming object"))
public class OER2RecordFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	public OER2RecordFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = OER2RecordFilter.INPUT_PORT_NAME)
	public void input(final OperationExecutionRecord record) {
		this.deliver(OUTPUT_PORT_NAME, new Record(record));
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}
