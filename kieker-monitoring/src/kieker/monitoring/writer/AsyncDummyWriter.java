/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * A writer that does nothing but asynchronously consuming records.
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
public class AsyncDummyWriter extends AbstractAsyncWriter {
	public AsyncDummyWriter(final Configuration configuration) {
		super(configuration);
		// nothing else to do here
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new AsyncDummyWriterThread(this.monitoringController, this.blockingQueue));
		this.addWorker(new AsyncDummyWriterThread(this.monitoringController, this.prioritizedBlockingQueue));
	}
}

/**
 * A writer thread that does nothing but consuming records.
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
class AsyncDummyWriterThread extends AbstractAsyncThread {

	public AsyncDummyWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue) {
		super(monitoringController, writeQueue);
		// noting else to do here
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		// nothing to do here
	}

	@Override
	protected void cleanup() {
		// nothing to clean up
	}
}
