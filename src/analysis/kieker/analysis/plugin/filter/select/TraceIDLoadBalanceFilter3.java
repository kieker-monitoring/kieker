package kieker.analysis.plugin.filter.select;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;

/**
 * @author Nils Christian Ehmke
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = TraceIDLoadBalanceFilter3.OUTPUT_PORT_NAME_1, eventTypes = { Trace.class, AbstractTraceEvent.class }),
			@OutputPort(name = TraceIDLoadBalanceFilter3.OUTPUT_PORT_NAME_2, eventTypes = { Trace.class, AbstractTraceEvent.class }),
			@OutputPort(name = TraceIDLoadBalanceFilter3.OUTPUT_PORT_NAME_3, eventTypes = { Trace.class, AbstractTraceEvent.class })
		})
public class TraceIDLoadBalanceFilter3 extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "monitoringRecordsFlow";

	public static final String OUTPUT_PORT_NAME_1 = "outputPort1";
	public static final String OUTPUT_PORT_NAME_2 = "outputPort2";
	public static final String OUTPUT_PORT_NAME_3 = "outputPort3";

	public TraceIDLoadBalanceFilter3(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = INPUT_PORT_NAME, eventTypes = { Trace.class, AbstractTraceEvent.class })
	public void inputOperationExecutionRecord(final IFlowRecord record) {
		final long traceId;

		if (record instanceof Trace) {
			traceId = ((Trace) record).getTraceId();
		} else if (record instanceof AbstractTraceEvent) {
			traceId = ((AbstractTraceEvent) record).getTraceId();
		} else {
			return;
		}

		switch ((int) (traceId % 3)) {
		case 0:
			super.deliver(OUTPUT_PORT_NAME_1, record);
			break;
		case 1:
			super.deliver(OUTPUT_PORT_NAME_2, record);
			break;
		case 2:
			super.deliver(OUTPUT_PORT_NAME_3, record);
			break;
		}
	}
}
