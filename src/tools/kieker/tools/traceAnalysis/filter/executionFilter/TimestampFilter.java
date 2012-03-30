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

package kieker.tools.traceAnalysis.filter.executionFilter;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTimestampFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their given tin and tout timestamps.
 * 
 * This class has exactly one input port and one output port. It receives only objects inheriting from the class {@link Execution}. If the received object is within
 * the defined timestamps, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@Plugin(outputPorts = @OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, description = "Fowards records within the timeperiod", eventTypes = { Execution.class }))
public class TimestampFilter extends AbstractTimestampFilter {

	public static final String INPUT_PORT_NAME_EXECUTION = "executions";

	public static final String OUTPUT_PORT_NAME_WITHIN_PERIOD = "executionsWithinTimePeriod";

	public static final String CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP = "ignoreExecutionsBeforeTimestamp";
	public static final String CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP = "ignoreExecutionsAfterTimestamp";

	/**
	 * Creates a new instance of the class {@link TimestampFilter} with the given parameters.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance. It should
	 *            contain the properties for the minimum and maximum timestamp.
	 */
	public TimestampFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = INPUT_PORT_NAME_EXECUTION, description = "Receives executions to be selected by their logging timestamps", eventTypes = { Execution.class })
	public void inputExecution(final Execution execution) {
		if (this.inRange(execution.getTin()) && this.inRange(execution.getTout())) {
			super.deliver(OUTPUT_PORT_NAME_WITHIN_PERIOD, execution);
		}
	}

	@Override
	protected String getConfigurationPropertyIgnoreBeforeTimestamp() {
		return CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP;
	}

	@Override
	protected String getConfigurationPropertyIgnoreAfterTimestamp() {
		return CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP;
	}
}
