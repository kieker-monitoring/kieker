/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.core.controller;

import java.lang.Thread.State;

import org.hamcrest.CoreMatchers;
import org.jctools.queues.MpscArrayQueue;
import org.junit.Assert;
import org.junit.Test;

import kieker.Await;
import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.dump.DumpWriter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class WriterControllerTest {

	private static final int THREAD_STATE_CHANGE_TIMEOUT_IN_MS = 1000;
	private static final long CONTROLLER_TIMEOUT_IN_MS = 1000;

	public WriterControllerTest() {
		super();
	}

	@Test
	@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
	// @SuppressFBWarnings("SIC")
	public void testBlockOnFailedInsertBehavior() throws Exception {
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DumpWriter.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_FQN,
				MpscArrayQueue.class.getName());
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_SIZE, "1");
		configuration.setProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");

		final WriterController writerController = new WriterController(configuration);

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// the first element fits into the queue
				writerController.newMonitoringRecord(new EmptyRecord());
				// the second element exceeds the queue's capacity and triggers the blocking wait
				writerController.newMonitoringRecord(new EmptyRecord());
			}
		});
		thread.start();

		Await.awaitThreadState(thread, State.WAITING, THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		writerController.init(); // starts the queue consumer

		Await.awaitThreadState(thread, State.TERMINATED, THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		writerController.cleanup(); // triggers the termination of the queue consumer

		writerController.waitForTermination(CONTROLLER_TIMEOUT_IN_MS);

		Assert.assertThat(writerController.getStateOfMonitoringWriterThread(), CoreMatchers.is(State.TERMINATED));
	}

}
