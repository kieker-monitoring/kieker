package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

public class MyWriter implements IMonitoringLogWriter {

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
		return true;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord arg0)
			throws MonitoringRecordReceiverException {
		Storage.container.add(arg0.toArray());
		return true;
	}

}
