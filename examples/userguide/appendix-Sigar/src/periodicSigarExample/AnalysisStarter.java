package periodicSigarExample;

import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.CPUUtilizationRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MemSwapUsageRecord;
import kieker.tools.util.LoggingTimestampConverter;

public class AnalysisStarter {

	public static void main(final String[] args)
			throws MonitoringReaderException, MonitoringRecordConsumerException {

		if (args.length == 0) {
			return;
		}

		/* Create Kieker.Analysis instance */
		final AnalysisController analysisInstance = new AnalysisController();
		/* Register our own consumer; set the max. response time to 1.9 ms */
		// TODO: Recover Consumer
		analysisInstance.registerPlugin(new StdOutDumpConsumer());

		/* Set filesystem monitoring log input directory for our analysis */
		final String inputDirs[] = { args[0] };
		analysisInstance.setReader(new FSReader(inputDirs));

		/* Start the analysis */
		analysisInstance.run();
	}
}

class StdOutDumpConsumer implements IMonitoringRecordConsumerPlugin {

	private final static Collection<Class<? extends IMonitoringRecord>> SUBSCRIPTION_LIST;
	static {
		SUBSCRIPTION_LIST = new ArrayList<Class<? extends IMonitoringRecord>>();
		StdOutDumpConsumer.SUBSCRIPTION_LIST.add(CPUUtilizationRecord.class);
		StdOutDumpConsumer.SUBSCRIPTION_LIST.add(MemSwapUsageRecord.class);
	}

	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return StdOutDumpConsumer.SUBSCRIPTION_LIST;
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof CPUUtilizationRecord) {
			final CPUUtilizationRecord cpuUtilizationRecord =
					(CPUUtilizationRecord) record;

			final String hostName = cpuUtilizationRecord.getHostName();
			final String cpuId = cpuUtilizationRecord.getCpuID();
			final double utilizationPercent = cpuUtilizationRecord.getTotalUtilization() * 100;
			
			System.out
					.println(String.format(
							"%s: [CPU] host: %s ; cpu-id: %s ; utilization: %3.2f %%",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(cpuUtilizationRecord
											.getTimestamp()),
							hostName, cpuId, utilizationPercent));
		} else if (record instanceof MemSwapUsageRecord) {
			final MemSwapUsageRecord memSwapUsageRecord =
					(MemSwapUsageRecord) record;
			
			final String hostName = memSwapUsageRecord.getHostName();
			final double memUsageMB = memSwapUsageRecord.getMemUsed() / (1024*1024);
			final double swapUsageMB = memSwapUsageRecord.getSwapUsed() / (1024*1024);
			
			System.out
					.println(String.format(
							"%s: [Mem/Swap] host: %s ; mem usage: %s MB ; swap usage: %s MB",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(memSwapUsageRecord
											.getTimestamp()),
									hostName, memUsageMB, swapUsageMB));
		} else {
			/* Unexpected record type */
			return false;
		}
		return true;
	}

	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// TODO Auto-generated method stub

	}
}