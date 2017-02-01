
package kieker.monitoring.core.controller;

import java.lang.Thread.State;

import org.jctools.queues.MpscArrayQueue;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writernew.dump.DumpWriter;

public class WriterControllerTest {

	private static final int THREAD_STATE_CHANGE_TIMEOUT_IN_MS = 1000;

	@Test
	public void testBlockOnFailedInsertBehavior() throws Exception {
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DumpWriter.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_FQN, MpscArrayQueue.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_SIZE, "1");
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");

		final WriterController writerController = new WriterController(configuration);

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				writerController.newMonitoringRecord(new EmptyRecord()); // the first element fits into the queue
				writerController.newMonitoringRecord(new EmptyRecord()); // the second element exceeds the queue's capacity and triggers the blocking wait
			}
		});
		thread.start();

		this.awaitThreadState(thread, State.WAITING, THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		writerController.init(); // starts the queue consumer

		this.awaitThreadState(thread, State.TERMINATED, THREAD_STATE_CHANGE_TIMEOUT_IN_MS);
	}

	private void awaitThreadState(final Thread thread, final State state, final int timeoutInMs) throws InterruptedException {
		final long interPauseTimeInMs = 10;

		int waitingTimeInMs = 0;
		while (thread.getState() != state) {
			if (waitingTimeInMs > timeoutInMs) {
				throw new AssertionError(
						"The given thread does not change its state to " + state + " within " + timeoutInMs + " ms. Current state: " + thread.getState());
			}
			Thread.sleep(interPauseTimeInMs);
			waitingTimeInMs += interPauseTimeInMs;
		}
	}
}
