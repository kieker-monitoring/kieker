package livedemo.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.jvm.ClassLoadingRecord;
import kieker.common.record.jvm.CompilationRecord;
import kieker.common.record.jvm.GCRecord;
import kieker.common.record.jvm.ThreadsStatusRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;

@Plugin(outputPorts = {
	@OutputPort(eventTypes = GCRecord.class, name = Distributor.OUTPUT_PORT_NAME_GC_RECORDS),
	@OutputPort(eventTypes = ClassLoadingRecord.class, name = Distributor.OUTPUT_PORT_NAME_CLASS_LOADING_RECORDS),
	@OutputPort(eventTypes = ThreadsStatusRecord.class, name = Distributor.OUTPUT_PORT_NAME_THREADS_STATUS_RECORDS),
	@OutputPort(eventTypes = CompilationRecord.class, name = Distributor.OUTPUT_PORT_NAME_COMPILATION_RECORDS),
	@OutputPort(eventTypes = CPUUtilizationRecord.class, name = Distributor.OUTPUT_PORT_NAME_CPU_UTILIZATION_RECORDS),
	@OutputPort(eventTypes = MemSwapUsageRecord.class, name = Distributor.OUTPUT_PORT_NAME_MEM_SWAP_USAGE_RECORDS),
	@OutputPort(eventTypes = OperationExecutionRecord.class, name = Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS),
})
public class Distributor extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "input";

	public static final String OUTPUT_PORT_NAME_GC_RECORDS = "gcRecords";
	public static final String OUTPUT_PORT_NAME_CLASS_LOADING_RECORDS = "classLoadingRecords";
	public static final String OUTPUT_PORT_NAME_THREADS_STATUS_RECORDS = "threadsStatusRecords";
	public static final String OUTPUT_PORT_NAME_COMPILATION_RECORDS = "compilationRecords";
	public static final String OUTPUT_PORT_NAME_CPU_UTILIZATION_RECORDS = "cpuUtilizationRecords";
	public static final String OUTPUT_PORT_NAME_MEM_SWAP_USAGE_RECORDS = "memSwapUsageRecords";
	public static final String OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS = "operationExecutionRecords";

	public Distributor(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = Distributor.INPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class)
	public void input(final IMonitoringRecord record) {
		if (record instanceof GCRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_GC_RECORDS, record);
		} else if (record instanceof ClassLoadingRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_CLASS_LOADING_RECORDS, record);
		} else if (record instanceof ThreadsStatusRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_THREADS_STATUS_RECORDS, record);
		} else if (record instanceof CompilationRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_COMPILATION_RECORDS, record);
		} else if (record instanceof CPUUtilizationRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_CPU_UTILIZATION_RECORDS, record);
		} else if (record instanceof MemSwapUsageRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_MEM_SWAP_USAGE_RECORDS, record);
		} else if (record instanceof OperationExecutionRecord) {
			super.deliver(Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS, record);
		}
	}

}
