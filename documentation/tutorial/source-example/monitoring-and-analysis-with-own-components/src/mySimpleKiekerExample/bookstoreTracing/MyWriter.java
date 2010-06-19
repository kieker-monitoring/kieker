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
		return null;
	}

	@Override
	public boolean init(String initString) {
		boolean result;
		try {
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
			pipe.put(record.toArray());
			result = true;
		} catch (InterruptedException e) {
			result = false;
		}
		return result;
	}

}
