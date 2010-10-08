/**
 * 
 */
package kieker.monitoring.writer;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

/**
 * A writer that does nothing but consuming records.
 * 
 * @author Andre van Hoorn
 * 
 */
public class DummyLogWriter implements IMonitoringLogWriter {

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return true; // we don't care about incoming records
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean init(final String initString) {
		return true; // no initialization required
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Vector<AbstractWorkerThread> getWorkers() {
		return null; // have no workers
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public String getInfoString() {
		return this.getClass().getName();
	}

}
