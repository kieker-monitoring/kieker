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

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * TODO: Current response time unit: Nanoseconds
 * TODO: Make source of timestamp configurable? (may be loggingTimestamp, tin, tout)
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(outputPorts = { @OutputPort(name = ResponseTimeExtractionFilter.OUTPUT_PORT_NAME_VALUE, eventTypes = { NamedDoubleTimeSeriesPoint.class }) })
public class ResponseTimeExtractionFilter extends AbstractFilterPlugin {
	// private static final Log LOG = LogFactory.getLog(ResponseTimeExtractionFilter.class);

	public static final String OUTPUT_PORT_NAME_VALUE = "outputResponseTime";
	public static final String INPUT_PORT_NAME_VALUE = "inputExecutionRecord";

	// TODO: Add configuration property for Time Unit

	public ResponseTimeExtractionFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = INPUT_PORT_NAME_VALUE, eventTypes = { OperationExecutionRecord.class })
	public void inputExecutionRecord(final OperationExecutionRecord execution) {
		final long toutMillis = TimeUnit.MILLISECONDS.convert(execution.getTout(), TimeUnit.NANOSECONDS);
		final Date time = new Date(toutMillis);
		final double responseTime = execution.getTout() - execution.getTin();
		final NamedDoubleTimeSeriesPoint tspoint = new NamedDoubleTimeSeriesPoint(time, responseTime, execution.getOperationSignature());
		super.deliver(OUTPUT_PORT_NAME_VALUE, tspoint);
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
