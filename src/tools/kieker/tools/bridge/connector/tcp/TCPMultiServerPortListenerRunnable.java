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
package kieker.tools.bridge.connector.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;

/**
 * @author Reiner Jung
 * @since 1.8
 */
public class TCPMultiServerPortListenerRunnable implements Runnable {

	private final ServerSocket serverSocket;
	private final BlockingQueue<IMonitoringRecord> recordQueue;
	private boolean active = true;
	private final ConcurrentMap<Integer, LookupEntity> lookupEntityMap;
	private final ExecutorService executor;

	public TCPMultiServerPortListenerRunnable(final int port, final BlockingQueue<IMonitoringRecord> recordQueue,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap, final ExecutorService executor) throws IOException {
		this.recordQueue = recordQueue;
		this.lookupEntityMap = lookupEntityMap;
		this.serverSocket = new ServerSocket(port);
		this.executor = executor;
	}

	public void run() {
		try {
			while (this.active) {
				// await client connections and start a connection handler
				this.executor.execute(new TCPMultiServerConnectionRunnable(this.serverSocket.accept(),
						this.lookupEntityMap,
						this.recordQueue));
			}
			this.serverSocket.close();
		} catch (final IOException e) {
			this.active = false;
		}
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
