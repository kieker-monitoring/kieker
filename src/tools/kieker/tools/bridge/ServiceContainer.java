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

package kieker.tools.bridge;

import java.util.ArrayList;
import java.util.Collection;

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

	protected boolean active; // is true when the service is running
	protected long recordCount; // counter for received records

	private final IServiceConnector service;

	private final Configuration configuration;
	private final Collection<IServiceListener> listeners;
	private long listenerUpdateInterval;
	private IMonitoringController kieker;
	private boolean respawn;

	/**
	 * @param configuration
	 *            A configuration object for Kieker monitoring
	 * @param service
	 *            A service component to handle incoming data
	 */
	public ServiceContainer(final Configuration configuration, final IServiceConnector service) {
		this.setRespawn(false);
		this.configuration = configuration;
		this.listeners = new ArrayList<IServiceListener>();
		this.listenerUpdateInterval = 100; // TODO: extract as constant DEFAULT INTERVAL or smthg.
		this.service = service;
	}

	/**
	 * Main loop of the Kieker bridge.
	 * 
	 * @throws Exception
	 *             is may throw a wide range of exceptions, depending on the implementation of deserialize()
	 */
	// TODO: maybe wrap all possible Exception into a custom one and throw taht one?
	public void run() throws Exception {
		this.kieker = MonitoringController.createInstance(this.configuration);
		do {
			this.service.setup();
			this.active = true;
			this.recordCount = 0;
			while (this.active) {
				final IMonitoringRecord record = this.service.deserialize();
				if (null != record) {
					this.kieker.newMonitoringRecord(record);
					this.recordCount++;
					if ((this.recordCount % this.listenerUpdateInterval) == 0) {
						this.updateState(null); // TODO: null as message?
					}
				} else {
					this.active = false;
				}
			}
			this.updateState(null); // TODO: null as message?
			this.service.close();
		} while (this.respawn);
		this.kieker.terminateMonitoring();
	}

	/**
	 * Safely end bridge loop. This routine should only be called from a signal handler or similar
	 * construct, as the run method waits for IO.
	 * 
	 * @throws Exception
	 *             transport exception from inner source
	 */
	public void shutdown() throws Exception {
		this.respawn = false;
		this.active = false;
		this.service.close(); // TODO: also called by main loop? also main loop might still access structures once?
		this.kieker.terminateMonitoring(); // TODO: also called by main loop? also main loop might still access structures once?
	}

	/**
	 * Informs all listeners about record count and an option message. The message may be null.
	 * 
	 * @param message
	 *            the message passed to all listeners. May be null.
	 */
	private void updateState(final String message) {
		for (final IServiceListener listener : this.listeners) {
			listener.handleEvent(this.recordCount, message);
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
		return this.recordCount;
	}

	public boolean isRespawn() {
		return this.respawn;
	}

	public void setRespawn(final boolean respawn) {
		this.respawn = respawn;
	}
}
