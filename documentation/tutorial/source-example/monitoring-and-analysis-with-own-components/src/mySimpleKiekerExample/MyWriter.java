package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

public class MyWriter implements IMonitoringLogWriter {

	private MyPipe pipe;

	@Override
	public String getInfoString() {
		return "MyWriter";
	}

	@Override
	public Vector<AbstractWorkerThread> getWorkers() {
		/* We don't have any worker threads, so we return just null. */
		return null;
	}

	@Override
	public boolean init(String initString) {
		boolean result;
		try {
			/*
			 * The init string should have the form
			 * "pipename | asyncRecordQueueSize=8000" or something similar. We
			 * are only interested in the name of the pipe.
			 */
			initString = initString.substring(0, initString.indexOf('|') - 1);
			/* Try to get the pipe via name. */
			pipe = MyNamedPipeManager.getInstance().acquirePipe(initString);
			result = true;
		} catch (IllegalArgumentException ex) {
			result = false;
		}
		return result;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record) {
		boolean result;
		try {
			/* Just write the content of the record into the pipe. */
			pipe.put(record.toArray());
			result = true;
		} catch (InterruptedException e) {
			result = false;
		}
		return result;
	}

}
