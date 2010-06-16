package mySimpleKiekerExample.bookstoreTracing;

import java.util.ArrayList;
import java.util.Iterator;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.IMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.common.record.OperationExecutionRecord;

public class MyReader extends AbstractMonitoringLogReader {

	ArrayList<IMonitoringRecordReceiver> receivers = new ArrayList<IMonitoringRecordReceiver>();

	@Override
	public void addRecordReceiver(IMonitoringRecordReceiver receiver) {
		receivers.add(receiver);
	}

	@Override
	public void init(String initString) throws IllegalArgumentException {

	}

	@Override
	public boolean read() throws MonitoringLogReaderException {
		Iterator<Object[]> iterator = Storage.container.iterator();

		while (iterator.hasNext()) {
			OperationExecutionRecord record = new OperationExecutionRecord();
			record.initFromArray(iterator.next());
			for (IMonitoringRecordReceiver receiver : receivers) {
				try {
					receiver.newMonitoringRecord(record);
				} catch (MonitoringRecordReceiverException ex) {
				}
			}
		}

		return true;
	}

}
