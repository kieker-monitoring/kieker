/***************************************************************************
 * Copyright 2013 Kieker Project (http://kiekerMonitoringController-monitoring.net)
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

package kieker.tools.bridge;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class ServiceContainer {

	// TODO: is concurrent access possible to any of these variables? (seems so!) -> volatile, other data structures, ...

	protected volatile boolean active; // is true when the service is running

	private static final long DEFAULT_LISTENER_UPDATE_INTERVAL = 100L;

	private final Collection<IServiceListener> listeners = new CopyOnWriteArrayList<IServiceListener>();
	private final IMonitoringController kiekerMonitoringController;
	private final IServiceConnector service;

	private volatile boolean respawn;
	private volatile long listenerUpdateInterval = DEFAULT_LISTENER_UPDATE_INTERVAL;

	/**
	 * @param configuration
	 *            A configuration object for Kieker monitoring
	 * @param service
	 *            A service component to handle incoming data
	 */
	public ServiceContainer(final Configuration configuration, final IServiceConnector service, final boolean respawn) {
		this.kiekerMonitoringController = MonitoringController.createInstance(configuration);
		this.respawn = respawn;
		this.service = service;
	}

	/**
	 * Main loop of the Kieker bridge.
	 * 
	 * @throws Exception
	 *             is may throw a wide range of exceptions, depending on the implementation of deserialize()
	 */
	// TODO: maybe wrap all possible Exception into a custom one and throw that one?
	public void run() throws Exception {
		do {
			this.updateState("Starting service container.");
			long recordCounter = 0;
			this.service.setup();
			this.active = true;
			while (this.active) {
				final IMonitoringRecord record = this.service.deserializeNextRecord();
				if (null != record) { // TODO: Throw Exception instead of using if
					this.kiekerMonitoringController.newMonitoringRecord(record);
					if ((++recordCounter % this.listenerUpdateInterval) == 0) {
						this.updateState(this.listenerUpdateInterval + " records received.");
					}
				} else {
					this.active = false;
				}
			}
			this.updateState("Shutting service container down.");
			this.service.close();
		} while (this.respawn);

		this.kiekerMonitoringController.terminateMonitoring();
	}

	/**
	 * Safely end bridge loop. This routine should only be called from a signal handler or similar
	 * construct, as the run method waits for IO.
	 * 
	 * @throws Exception
	 *             transport exception from inner source
	 */
	public void shutdown() throws Exception {
		this.active = false;
		this.respawn = false;
		this.service.close(); // TODO: also called by main loop? also main loop might still access structures once?
		this.kiekerMonitoringController.terminateMonitoring(); // TODO: also called by main loop? also main loop might still access structures once?
	}

	/**
	 * Informs all listeners about record count and an option message. The message may be null.
	 * 
	 * @param message
	 *            the message passed to all listeners. May be null.
	 */
	private void updateState(final String message) {
		for (final IServiceListener listener : this.listeners) {
			listener.handleEvent(this.kiekerMonitoringController.getNumberOfInserts(), message);
		}
	}

	/**
	 * Add an update state listener.
	 * 
	 * @param listener
	 *            an object implementing the IServiceListener interface
	 */
	public void addListener(final IServiceListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Set the update interval for the listener information. The default is 100 records.
	 * 
	 * @param listenerUpdateInterval
	 *            the new update interval in number of records
	 */
	public void setListenerUpdateInterval(final long listenerUpdateInterval) {
		this.listenerUpdateInterval = listenerUpdateInterval;
	}

	public long getRecordCount() {
		return this.kiekerMonitoringController.getNumberOfInserts();
	}

	public boolean isRespawn() {
		return this.respawn;
	}
}
