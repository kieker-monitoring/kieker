package mySimpleKiekerExample.bookstoreTracing;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;

public class MyReader extends AbstractMonitoringLogReader {
	private MyPipe pipe;

	@Override
	public void init(String initString) throws IllegalArgumentException {
		pipe = MyNamedPipeManager.getInstance().acquirePipe(initString);
	}

	@Override
	public boolean read() throws MonitoringLogReaderException {
		boolean result;
		try {
			Object[] obj;
			while ((obj = pipe.poll(4)) != null) {
				MyRecord record = new MyRecord();
				record.initFromArray(obj);
				deliverRecord(record);
			}
			result = true;
		} catch (InterruptedException e) {
			result = false;
		}
		return result;
	}
}
