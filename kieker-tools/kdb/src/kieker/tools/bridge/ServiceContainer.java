/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import kieker.monitoring.core.controller.MonitoringController;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * Container for the Kieker Data Bridge handling the startup and shutdown of Kieker and the service connector.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public class ServiceContainer {

	/**
	 * Update interval for the process listener. The process listener is mainly used in verbose mode or in UI
	 * applications utilizing a ServiceContainer.
	 */
	public static final long DEFAULT_LISTENER_UPDATE_INTERVAL = 100L;

	/**
	 * Is true when the service is running.
	 */
	protected volatile boolean active;

	private final Collection<IServiceListener> listeners = new CopyOnWriteArrayList<>();
	private final MonitoringController kiekerMonitoringController;
	private final IServiceConnector service;

	private volatile boolean respawn;
	private volatile long listenerUpdateInterval = DEFAULT_LISTENER_UPDATE_INTERVAL;

	private int numRecordsReceived;

	/**
	 * @param configuration
	 *            A configuration object for Kieker monitoring
	 * @param service
	 *            A service component to handle incoming data
	 * @param respawn
	 *            Respawn the connector if it fails (this construct is debatable it should be handled by the connector
	 *            itself)
	 */
	public ServiceContainer(final Configuration configuration, final IServiceConnector service, final boolean respawn) {
		this.kiekerMonitoringController = MonitoringController.createInstance(configuration);
		this.respawn = respawn;
		this.service = service;
	}

	/**
	 * Main loop of the Kieker bridge.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if deserializeNextRecord exits with a ConnectorDataTransmissionException
	 */
	public void run() throws ConnectorDataTransmissionException {
		do {
			this.updateState("Starting service container.");
			this.service.initialize();
			this.active = true;
			while (this.active) {
				try {
					this.kiekerMonitoringController.newMonitoringRecord(this.service.deserializeNextRecord());
					this.numRecordsReceived++;
					if ((this.numRecordsReceived % this.listenerUpdateInterval) == 0) {
						this.updateState(this.listenerUpdateInterval + " records received.");
					}
				} catch (final ConnectorEndOfDataException e) {
					this.active = false;
				}
			}
			this.updateState("Shutting service container down.");
			this.service.close();
		} while (this.respawn);

		this.kiekerMonitoringController.terminateMonitoring();
		try {
			// we expect a waiting time of 10-100 ms.
			// So, a timeout of 10,000 ms should be high enough.
			this.kiekerMonitoringController.waitForTermination(10000);
		} catch (final InterruptedException e) {
			throw new IllegalStateException("Exception occured while waiting for the monitoring to terminate.", e);
		}
	}

	/**
	 * Safely end bridge loop. This routine should only be called from the shutdown hook thread in the main part of a
	 * server. In other cases it will result in strange runtime errors.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             An error occurred during data transmission and in this particular case while closing the data
	 *             transmission.
	 */
	public void shutdown() throws ConnectorDataTransmissionException {
		this.active = false;
		this.respawn = false;
		this.service.close();
		this.kiekerMonitoringController.terminateMonitoring();
	}

	/**
	 * Informs all listeners about record count and an option message. The message may be null.
	 *
	 * @param message
	 *            the message passed to all listeners. May be null.
	 */
	private void updateState(final String message) {
		for (final IServiceListener listener : this.listeners) {
			listener.handleEvent(this.numRecordsReceived, message);
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
		return this.numRecordsReceived;
	}

	public boolean isRespawn() {
		return this.respawn;
	}

	// /**
	// * Used in tests only.
	// */
	// /* default */ MonitoringController getKiekerMonitoringController() {
	// return this.kiekerMonitoringController;
	// }
}
