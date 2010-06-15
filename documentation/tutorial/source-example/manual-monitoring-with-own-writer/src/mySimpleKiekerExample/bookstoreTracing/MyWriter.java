package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

public class MyWriter implements IMonitoringLogWriter {

	@Override
	public String getInfoString() {
		/* Return just the name of this class as info string. */
		return "MyWriter";
	}

	@Override
	public Vector<AbstractWorkerThread> getWorkers() {
		/* We don't use workers, so we just return null. */
		return null;
	}

	@Override
	public boolean init(String initString) {
		/*
		 * There is nothing to initialize, so we inform the calling method that
		 * everything went well.
		 */
		return true;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record)
			throws MonitoringRecordReceiverException {
		/*
		 * Instead of persisting the given record, we put the content just on
		 * the screen.
		 */
		/* Get the content. */
		Object content[] = record.toArray();
		/*
		 * Call the toString()-method for every part and put the result in one
		 * big string. Separate the content with a comma.
		 */
		int size = content.length;
		String s = "[";
		for (int i = 0; i < size - 1; i++) {
			s += content[i].toString() + ", ";
		}
		if (size > 0) {
			s += content[size - 1];
		}
		s += "]";
		/* Now show the string with a time stamp on the screen. */
		System.out.println(record.getLoggingTimestamp() + ": " + s);
		/* Everything went well, so we return true. */
		return true;
	}
}
