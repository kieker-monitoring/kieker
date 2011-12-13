package kieker.test.analysis.junit.util;

import java.util.List;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.AInputPort;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

public class MonitoringSinkClass extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "doJob";
	private final List<IMonitoringRecord> receivedRecords;

	public MonitoringSinkClass(final List<IMonitoringRecord> receivedRecords) {
		super(new Configuration());
		this.receivedRecords = receivedRecords;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return null;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return null;
	}

	@AInputPort(eventTypes = { IMonitoringRecord.class })
	public void doJob(final Object data) {
		this.receivedRecords.add((IMonitoringRecord) data);
	}
}
