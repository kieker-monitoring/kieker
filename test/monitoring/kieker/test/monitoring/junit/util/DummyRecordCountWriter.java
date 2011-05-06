package kieker.test.monitoring.junit.util;

import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.DummyWriter;

/**
 * A writer that simply counts the number of records of type DummyRecord received.
 * 
 * @author Andre van Hoorn, Jan Waller 
 */
public final class DummyRecordCountWriter extends DummyWriter {
	private final AtomicInteger numDummyRecords = new AtomicInteger(0);

	public DummyRecordCountWriter(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * @return the number of records
	 */
	public final int getNumDummyRecords() {
		return this.numDummyRecords.get();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof DummyRecord) {
			this.numDummyRecords.incrementAndGet();
		}
		return super.newMonitoringRecord(record);
	}
}
