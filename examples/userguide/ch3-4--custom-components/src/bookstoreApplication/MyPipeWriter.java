package bookstoreApplication;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

public class MyPipeWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = MyPipeWriter.class.getName() + ".";
	private static final String PROPERTY_PIPE_NAME = MyPipeWriter.PREFIX
			+ "pipeName";
	private volatile String pipeName;
	private volatile MyPipe pipe;

	public MyPipeWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			/* Just write the content of the record into the pipe. */
			this.pipe.put(record.toArray());
		} catch (final InterruptedException e) {
			return false; // signal error
		}
		return true;
	}

	@Override
	protected void init() throws Exception {
		this.pipeName =
				this.configuration
						.getStringProperty(MyPipeWriter.PROPERTY_PIPE_NAME);
		this.pipe = MyNamedPipeManager.getInstance().acquirePipe(this.pipeName);
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}
}
