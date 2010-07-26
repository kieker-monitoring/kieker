package mySimpleKiekerExample;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;

public class MyReader extends AbstractMonitoringLogReader {
	private MyPipe pipe;

	@Override
	public void init(String initString) throws IllegalArgumentException {
		/* Try to get the pipe via the given name. */
		pipe = MyNamedPipeManager.getInstance().acquirePipe(initString);
	}

	@Override
	public boolean read() throws MonitoringLogReaderException {
		boolean result;
		try {
			Object[] obj;
			/* Wait 4 seconds at maximum for the next data. */
			while ((obj = pipe.poll(4)) != null) {
				/* Try to create a new record out of it... */
				MyRecord record = new MyRecord();
				record.initFromArray(obj);
				/* ...and delegate the task of delivering to the super class. */
				deliverRecord(record);
			}
			result = true;
		} catch (InterruptedException e) {
			result = false;
		}
		return result;
	}
}
