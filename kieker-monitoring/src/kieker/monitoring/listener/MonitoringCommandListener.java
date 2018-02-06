/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.listener;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * Represents a listener which is informed upon a event is received, which should .
 *
 * @author Marc Adolf
 * @since 1.13
 *
 */
public class MonitoringCommandListener implements IRecordReceivedListener {

	private static final Log LOG = LogFactory.getLog(MonitoringCommandListener.class);
	private final MonitoringController monitoringController;

	/**
	 * Creates a new listener for {@link RemoteControlEvent RemoteControlEvents}. Relies on an existing {@link MonitoringController} to transfer messages like the
	 * (de-)activation of probes.
	 *
	 * @param monitoringController
	 */
	public MonitoringCommandListener(final MonitoringController monitoringController) {
		this.monitoringController = monitoringController;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.IRecordReceivedListener#onRecordReceived(kieker.common.record.IMonitoringRecord)
	 */
	@Override
	public void onRecordReceived(final IMonitoringRecord record) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Received new record: " + record.getClass().getName());
		}
		if (!(record instanceof IRemoteControlEvent)) {
			LOG.info("Received an event for the TCP monitoring controller, which is no remote control event");
		}
		final String pattern = ((IRemoteControlEvent) record).getPattern();
		if (record instanceof DeactivationEvent) {
			this.monitoringController.deactivateProbe(pattern);
		} else if (record instanceof ActivationEvent) {
			this.monitoringController.activateProbe(pattern);
		} else {
			LOG.info("Received unknown remote control event: " + record.getClass().getName()); // NOPMD
		}
	}

}
