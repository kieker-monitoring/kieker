package kieker.analysis.reader;

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.common.record.OperationExecutionRecord;

/**
 * This is a logReader for testing. It creates just some random data. All have
 * the same operation and component and some random wait time of approx. a
 * second. It never terminates.
 * 
 * @author matthias
 */
public class DummyLogReader extends AbstractMonitoringReader {

	private final int i = 1;

	@Override
	/**
	 * It never finished to produce dummy data about each second
	 */
	public boolean read() {
		while (true) {
			final long startTime = System.nanoTime();

			// wait a bit
			final long sleeptime = Math.round(Math.random() * 750d + 250d);
			try {
				Thread.sleep(sleeptime);
			} catch (final InterruptedException ex) {
				Logger.getLogger(DummyLogReader.class.getName()).log(
						Level.SEVERE, null, ex);
			}

			final OperationExecutionRecord testRecord = new OperationExecutionRecord(
					"ComponentA", "OperationA", this.i, startTime,
					System.nanoTime());
			this.deliverRecord(testRecord);
		}
	}

	@Override
	public boolean init(final String initString) throws IllegalArgumentException {
		// nothing to do...
		return true;
	}
}
