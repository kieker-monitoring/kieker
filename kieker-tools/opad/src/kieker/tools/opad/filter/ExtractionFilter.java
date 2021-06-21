/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.opad.record.NamedDoubleRecord;

/**
 * An instance of this class extracts the data from incoming records.
 * 
 * @since 1.10
 * @author Tom Frotscher
 * 
 * @since 1.9
 */
@Plugin(outputPorts = { @OutputPort(name = ExtractionFilter.OUTPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleTimeSeriesPoint.class }) },
		configuration = {
			@Property(name = ExtractionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = ExtractionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT)
		})
public class ExtractionFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_VALUE = "inputRecord";
	public static final String OUTPUT_PORT_NAME_VALUE = "outputData";

	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS";

	private final TimeUnit timeunit;

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

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());

		return configuration;
	}

	@InputPort(name = INPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleRecord.class })
	public void inputExecutionRecord(final NamedDoubleRecord record) {
		final long timestampMillis = this.timeunit.convert(record.getTimestamp(), super.recordsTimeUnitFromProjectContext);
		final NamedDoubleTimeSeriesPoint tspoint = new NamedDoubleTimeSeriesPoint(timestampMillis, record.getResponseTime(), record.getApplicationName());

		super.deliver(OUTPUT_PORT_NAME_VALUE, tspoint);
	}

}
