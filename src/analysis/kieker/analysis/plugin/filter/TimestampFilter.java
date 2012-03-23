package kieker.analysis.plugin.filter;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;

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
	@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME, description = "records within the timeperiod", eventTypes = { IMonitoringRecord.class }),
	@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME_NOT, description = "records out of the timeperiod", eventTypes = { IMonitoringRecord.class })
})
public final class TimestampFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "input-records";
	public static final String INPUT_PORT_NAME_COMBINED = "input-combined";
	public static final String INPUT_PORT_NAME_FLOW = "input-flow-records";
	public static final String INPUT_PORT_NAME_EXECUTION = "input-execution-records";
	public static final String OUTPUT_PORT_NAME = "output";
	public static final String OUTPUT_PORT_NAME_NOT = "output-not";

	public static final String CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = "ignoreBeforeTimestamp";
	public static final String CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP = "ignorAfterTimestamp";

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreBeforeTimestamp;
	private final long ignoreAfterTimestamp;

	public TimestampFilter(final Configuration configuration) {
		super(configuration);
		this.ignoreBeforeTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP);
		this.ignoreAfterTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(TimestampFilter.MIN_TIMESTAMP));
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(TimestampFilter.MAX_TIMESTAMP));
		return configuration;
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(this.ignoreBeforeTimestamp));
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(this.ignoreAfterTimestamp));
		return configuration;
	}

	private final boolean inRange(final long timestamp) {
		return (timestamp >= this.ignoreBeforeTimestamp) && (timestamp <= this.ignoreAfterTimestamp);
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_COMBINED, description = "combined input", eventTypes = { IMonitoringRecord.class })
	public void inputCombined(final IMonitoringRecord record) {
		if (record instanceof OperationExecutionRecord) {
			this.inputOperationExecutionRecord((OperationExecutionRecord) record);
		} else if (record instanceof AbstractTraceEvent) {
			this.inputTraceEvent((AbstractTraceEvent) record);
		} else {
			this.inputIMonitoringRecord(record);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME, description = "IMonitoringRecord input", eventTypes = { IMonitoringRecord.class })
	public final void inputIMonitoringRecord(final IMonitoringRecord record) {
		if (this.inRange(record.getLoggingTimestamp())) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME, record);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_NOT, record);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_FLOW, description = "TraceEvent input", eventTypes = { AbstractTraceEvent.class })
	public final void inputTraceEvent(final AbstractTraceEvent event) {
		if (this.inRange(event.getTimestamp())) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME, event);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_NOT, event);
		}
	}

	@InputPort(name = TimestampFilter.INPUT_PORT_NAME_EXECUTION, description = "Execution input (incorporates the records 'tin' and 'tout' timestamps)", eventTypes = { OperationExecutionRecord.class })
	public final void inputOperationExecutionRecord(final OperationExecutionRecord execution) {
		if (this.inRange(execution.getTin()) && this.inRange(execution.getTout())) {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME, execution);
		} else {
			super.deliver(TimestampFilter.OUTPUT_PORT_NAME_NOT, execution);
		}
	}
}
