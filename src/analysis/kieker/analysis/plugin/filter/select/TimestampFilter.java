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

package kieker.analysis.plugin.filter.select;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;

/**
 * Allows to filter {@link IMonitoringRecord} objects based on their given timestamps.
 * 
 * This class has several specialized input ports and a single output port.
 * 
 * If the received record is within the defined timestamps, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(outputPorts = {
	@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, description = "Fowards records within the timeperiod", eventTypes = { IMonitoringRecord.class }),
	@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME_OUTSIDE_PERIOD, description = "Forwards records out of the timeperiod", eventTypes = { IMonitoringRecord.class })
})
public final class TimestampFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_ANY_RECORD = "monitoring-records-any";
	public static final String INPUT_PORT_NAME_FLOW = "monitoring-records-flow";
	public static final String INPUT_PORT_NAME_EXECUTION = "monitoring-records-execution";
	public static final String INPUT_PORT_NAME_COMBINED = "monitoring-records-combined";

	public static final String OUTPUT_PORT_NAME_WITHIN_PERIOD = "records-within-time-period";
	public static final String OUTPUT_PORT_NAME_OUTSIDE_PERIOD = "records-outside-period";

	public static final String CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP = "ignore-before-timestamp";
	public static final String CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP = "ignore-after-timestamp";

	public static final long CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP = 0;

	private final long ignoreBeforeTimestamp;
	private final long ignoreAfterTimestamp;

	public TimestampFilter(final Configuration configuration) {
		super(configuration);
		this.ignoreBeforeTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP);
		this.ignoreAfterTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP);
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(TimestampFilter.CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP));
		configuration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(TimestampFilter.CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP));
		return configuration;
	}

	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(this.ignoreBeforeTimestamp));
		configuration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(this.ignoreAfterTimestamp));
		return configuration;
	}

	private final boolean inRange(final long timestamp) {
		return (timestamp >= this.ignoreBeforeTimestamp) && (timestamp <= this.ignoreAfterTimestamp);
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_COMBINED, description = "Receives records to be selected by timestamps, based on type-specific selectors", eventTypes = { IMonitoringRecord.class })
	public void inputCombined(final IMonitoringRecord record) {
		if (record instanceof OperationExecutionRecord) {
			this.inputOperationExecutionRecord((OperationExecutionRecord) record);
		} else if (record instanceof AbstractTraceEvent) {
			this.inputTraceEvent(record);
		} else {
			this.inputIMonitoringRecord(record);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_ANY_RECORD, description = "Receives records to be selected by their logging timestamps", eventTypes = { IMonitoringRecord.class })
	public final void inputIMonitoringRecord(final IMonitoringRecord record) {
		if (this.inRange(record.getLoggingTimestamp())) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, record);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_OUTSIDE_PERIOD, record);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_FLOW, description = "Receives trace events to be selected by a specific timestamp selector",
			eventTypes = { AbstractTraceEvent.class, Trace.class })
	public final void inputTraceEvent(final IMonitoringRecord record) {
		final long timestamp;

		if (record instanceof Trace) {
			timestamp = ((Trace) record).getLoggingTimestamp();
		} else if (record instanceof AbstractTraceEvent) {
			timestamp = ((AbstractTraceEvent) record).getTimestamp();
		} else {
			// should not happen given the accepted type
			return;
		}

		if (this.inRange(timestamp)) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, record);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_OUTSIDE_PERIOD, record);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_EXECUTION, description = "Receives trace events to be selected by a specific timestamp selector (based on tin and tout)", eventTypes = { OperationExecutionRecord.class })
	public final void inputOperationExecutionRecord(final OperationExecutionRecord execution) {
		if (this.inRange(execution.getTin()) && this.inRange(execution.getTout())) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, execution);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_OUTSIDE_PERIOD, execution);
		}
	}
}
