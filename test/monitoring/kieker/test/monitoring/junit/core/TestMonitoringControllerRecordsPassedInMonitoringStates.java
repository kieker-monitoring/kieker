/**
 * 
 */
package kieker.test.monitoring.junit.core;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyLogWriter;

/**
 * @author Andre van Hoorn
 * 
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates extends
		TestCase {
	public void testRecordsPassedToWriterWhenEnabled() {
		final MyDummyRecordCountWriter countWriter = new MyDummyRecordCountWriter();
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("MyName", countWriter);
		final IMonitoringController ctrl = new MonitoringController(config);

		Assert.assertTrue("Failed to enable monitoring",
				ctrl.enableMonitoring());
		ctrl.newMonitoringRecord(new MyDummyRecord());

		Assert.assertEquals("Unexpected number of records received", 1,
				countWriter.getNumDummyRecords());
	}

	public void testNoRecordsPassedToWriterWhenDisabled() {
		final MyDummyRecordCountWriter countWriter = new MyDummyRecordCountWriter();
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("MyName", countWriter);
		final IMonitoringController ctrl = new MonitoringController(config);

		Assert.assertTrue("Failed to disable monitoring",
				ctrl.disableMonitoring());
		ctrl.newMonitoringRecord(new MyDummyRecord());

		Assert.assertEquals("Unexpected number of records received", 0,
				countWriter.getNumDummyRecords());
	}

	public void testNoRecordsPassedToWriterWhenTerminated() {
		final MyDummyRecordCountWriter countWriter = new MyDummyRecordCountWriter();
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("MyName", countWriter);
		final IMonitoringController ctrl = new MonitoringController(config);

		ctrl.terminateMonitoring();
		ctrl.newMonitoringRecord(new MyDummyRecord());

		Assert.assertEquals("Unexpected number of records received", 0,
				countWriter.getNumDummyRecords());
	}
}

class MyDummyRecord extends DummyMonitoringRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

/**
 * A writer that simply counts the number of records of type My received.
 * 
 * @author Andre van Hoorn
 * 
 */
class MyDummyRecordCountWriter extends DummyLogWriter {
	private final AtomicInteger numDummyRecords = new AtomicInteger(0);

	/**
	 * @return the numRecords
	 */
	public int getNumDummyRecords() {
		return this.numDummyRecords.get();
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof MyDummyRecord) {
			this.numDummyRecords.incrementAndGet();
		}
		return super.newMonitoringRecord(record);
	}
}
