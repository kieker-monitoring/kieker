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

package kieker.tools.traceAnalysis.filter.executionFilter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their given tin and tout timestamps.
 * 
 * This class has exactly one input port and one output port. It receives only objects inheriting from the class {@link Execution}. If the received object is within
 * the defined timestamps, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 * 
 * @deprecated To be removed in Kieker 1.8 (Use {@link kieker.analysis.plugin.filter.select.TimestampFilter} instead)
 */
@SuppressWarnings("deprecation")
@Deprecated
@Plugin(description = "A filter allowing to filter incoming execution objects based on their timestamps",
		outputPorts = {
			@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, description = "Fowards records within the timeperiod",
					eventTypes = { Execution.class })
		},
		configuration = {
			@Property(name = TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, defaultValue = "0"),
			@Property(name = TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, defaultValue = "9223372036854775807") // Long.toString(Long.MAX_VALUE)
		})
public class TimestampFilter extends kieker.tools.traceAnalysis.filter.AbstractTimestampFilter {

	/** This is the name of the input port receiving new executions. */
	public static final String INPUT_PORT_NAME_EXECUTION = "executions";

	/** The name of the output port delivering the executions which are within the defined time limits. */
	public static final String OUTPUT_PORT_NAME_WITHIN_PERIOD = "executionsWithinTimePeriod";

	/** The name of the property determining the lower limit of the filter. */
	public static final String CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP = "ignoreExecutionsBeforeTimestamp";
	/** The name of the property determining the upper limit of the filter. */
	public static final String CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP = "ignoreExecutionsAfterTimestamp";

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TimestampFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Creates a new instance of the class {@link TimestampFilter} with the given parameters.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance. It should
	 *            contain the properties for the minimum and maximum timestamp.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public TimestampFilter(final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * This method represents the input port of this filter, processing incoming execution objects.
	 * 
	 * @param execution
	 *            The next execution object.
	 */
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
