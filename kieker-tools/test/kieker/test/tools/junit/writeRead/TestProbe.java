package kieker.test.tools.junit.writeRead;

import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

public class TestProbe {

	private final IMonitoringController monitoringController;

	public TestProbe(final IMonitoringController monitoringController) {
		super();
		this.monitoringController = monitoringController;
	}

	public void triggerRecords(final List<IMonitoringRecord> records) {
		for (final IMonitoringRecord record : records) {
			this.monitoringController.newMonitoringRecord(record);
		}
	}
}
