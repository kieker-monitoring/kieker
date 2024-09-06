/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.Await;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.dump.DumpWriter;

/**
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class MonitoringWriterThreadTest {

	private static final int THREAD_STATE_CHANGE_TIMEOUT_IN_MS = 1000;

	static {
		final String rootExecutionDir = MonitoringWriterThreadTest.class.getResource("/").getFile();
		// does not work */ System.setProperty("java.util.logging.config.file", "logging.properties");
		// works */System.setProperty("java.util.logging.config.file",
		// "I:/Repositories/kieker-hstr/build-eclipse/logging.properties");
		// works
		System.setProperty("java.util.logging.config.file", rootExecutionDir + "logging.properties");
		// does not work */System.setProperty("java.util.logging.config.file", "/logging.properties");
		// System.out.println("current dir (.): " + new File(".").getAbsolutePath());
		// System.out.println("current dir: " + MonitoringWriterThreadTest.class.getResource("/").getFile());
	}

	public MonitoringWriterThreadTest() {
		super();
	}

	@Test
	public void testTermination() throws Exception {
		final Configuration configuration = new Configuration();
		final AbstractMonitoringWriter writer = new DumpWriter(configuration);
		final BlockingQueue<IMonitoringRecord> writerQueue = new LinkedBlockingQueue<>();

		writerQueue.add(new EmptyRecord());

		final MonitoringWriterThread thread = new MonitoringWriterThread(writer, writerQueue);
		thread.start();

		while (!writerQueue.isEmpty()) {
			Thread.yield();
		}
		// thread terminates before the timeout has been reached, i.e.,
		// it correctly writes out the EmptyRecord from the writerQueue

		thread.terminate();
		thread.join(THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		Assert.assertThat(thread.getState(), CoreMatchers.is(State.TERMINATED));
	}

	@Test
	public void testBlocking() throws Exception {
		final Configuration configuration = new Configuration();
		final AbstractMonitoringWriter writer = new DumpWriter(configuration);
		final BlockingQueue<IMonitoringRecord> writerQueue = new LinkedBlockingQueue<>();

		final MonitoringWriterThread thread = new MonitoringWriterThread(writer, writerQueue);
		thread.start();

		Await.awaitThreadState(thread, State.WAITING, THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		thread.terminate();
		thread.join(THREAD_STATE_CHANGE_TIMEOUT_IN_MS);

		Assert.assertThat(thread.getState(), CoreMatchers.is(State.TERMINATED));
	}
}
