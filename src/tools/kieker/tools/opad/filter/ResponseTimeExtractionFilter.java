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

package kieker.tools.opad.filter;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * TODO: Make source of timestamp configurable? (may be loggingTimestamp, tin, tout)
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(outputPorts = { @OutputPort(name = ResponseTimeExtractionFilter.OUTPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleTimeSeriesPoint.class }) },
		configuration = {
			@Property(name = ResponseTimeExtractionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = "NANOSECONDS")
		})
public class ResponseTimeExtractionFilter extends AbstractFilterPlugin {
	// private static final Log LOG = LogFactory.getLog(ResponseTimeExtractionFilter.class);

	public static final String OUTPUT_PORT_NAME_VALUE = "outputResponseTime";
	public static final String INPUT_PORT_NAME_VALUE = "inputExecutionRecord";
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";

	private TimeUnit timeunit = TimeUnit.NANOSECONDS;

	// TODO: Add configuration property for Time Unit

	public ResponseTimeExtractionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = this.timeunit;
		}
		this.timeunit = configTimeunit;

	}

	@InputPort(name = INPUT_PORT_NAME_VALUE, eventTypes = { OperationExecutionRecord.class })
	public void inputExecutionRecord(final OperationExecutionRecord execution) {
		final long toutMillis = TimeUnit.MILLISECONDS.convert(execution.getTout(), TimeUnit.NANOSECONDS);
		final Date time = new Date(toutMillis);
		// ResponseTime in Nanoseconds
		final long responseTime = execution.getTout() - execution.getTin();
		// Convert the Responsetimes from Nanoseconds to Configurable TimeUnit
		final double responseTimeConfigurableTimeunit = this.timeunit.convert(responseTime, TimeUnit.NANOSECONDS);
		final NamedDoubleTimeSeriesPoint tspoint = new NamedDoubleTimeSeriesPoint(time, responseTimeConfigurableTimeunit, execution.getOperationSignature());
		super.deliver(OUTPUT_PORT_NAME_VALUE, tspoint);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		return configuration;
	}
}
