/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.filter;

import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.NamedDoubleRecord;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * An instance of this class extracts the Data from the incoming Records.
 * 
 * @author Tom Frotscher
 * @since 1.9
 */
@Plugin(outputPorts = { @OutputPort(name = ExtractionFilter.OUTPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleTimeSeriesPoint.class }) },
		configuration = {
			@Property(name = ExtractionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = "NANOSECONDS")
		})
public class ExtractionFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the ResponseTimeDoubleRecords.
	 */
	public static final String INPUT_PORT_NAME_VALUE = "inputRecord";

	/**
	 * Name of the output port delivering the response times.
	 */
	public static final String OUTPUT_PORT_NAME_VALUE = "outputData";

	/**
	 * Name of the property determining the time unit of the response times.
	 */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";

	private final TimeUnit timeunit;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public ExtractionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = configTimeunit;

	}

	/**
	 * This method represents the input port for incoming Records.
	 * 
	 * @param record
	 *            Incoming Record
	 */
	@InputPort(name = INPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleRecord.class })
	public void inputExecutionRecord(final NamedDoubleRecord record) {
		final long timestampMillis = this.timeunit.convert(record.getTimestamp(), TimeUnit.NANOSECONDS);
		final NamedDoubleTimeSeriesPoint tspoint = new NamedDoubleTimeSeriesPoint(timestampMillis, record.getValue(), record.getApplication());
		super.deliver(OUTPUT_PORT_NAME_VALUE, tspoint);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		return configuration;
	}
}
