/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kieker.common.configuration.Configuration;

/**
 * This class is part of the Master's thesis "Development of a Concurrent and Distributed Pipes and Filters Analysis Framework for Kieker".
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class ConcurrentCompositeAnalysisFilter extends AbstractCompositeAnalysisFilter {

	private final BlockingQueue<Object> receiverQueue = new LinkedBlockingQueue<Object>();

	public ConcurrentCompositeAnalysisFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public boolean init() {
		new SenderThread().start();

		return true;
	}

	@Override
	protected void handleIncomingData(final Object data) {
		this.receiverQueue.add(data);
	}

	@Override
	protected void handleOutgoingData(final Object data) {
		super.deliver(OUTPUT_PORT_NAME_EVENTS, data);
	}

	class SenderThread extends Thread {

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			while (true) {
				Object obj;
				try {
					obj = ConcurrentCompositeAnalysisFilter.this.receiverQueue.take();
					ConcurrentCompositeAnalysisFilter.this.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, obj);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
