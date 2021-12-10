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

package kieker.monitoring.listener;

import java.util.Arrays;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.ActivationParameterEvent;
import kieker.common.record.remotecontrol.AddParameterValueEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;
import kieker.common.record.remotecontrol.RemoveParameterValueEvent;
import kieker.common.record.remotecontrol.UpdateParameterEvent;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * Represents a listener which is informed upon a event is received, which
 * should.
 *
 * @author Marc Adolf
 * @since 1.14
 *
 */
public class MonitoringCommandListener implements IRecordReceivedListener {

	/**
	 * the logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringCommandListener.class);
	/**
	 * The corresponding {@link MonitoringController}.
	 */
	private final MonitoringController monitoringController;

	/**
	 * Creates a new listener for {@link RemoteControlEvent RemoteControlEvents}.
	 * Relies on an existing {@link MonitoringController} to transfer messages like
	 * the (de-)activation of probes.
	 *
	 * @param monitoringController
	 *            monitoring controller
	 */
	public MonitoringCommandListener(final MonitoringController monitoringController) {
		this.monitoringController = monitoringController;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.IRecordReceivedListener#onRecordReceived
	 * (kieker.common.record.IMonitoringRecord)
	 */
	@Override
	public void onRecordReceived(final IMonitoringRecord record) {
		MonitoringCommandListener.LOGGER.debug("Received new record: {}", record.getClass().getName());

		if (!(record instanceof IRemoteControlEvent)) {
			MonitoringCommandListener.LOGGER
					.info("Received an event for the TCP monitoring controller, which is no remote control event");
		}
		final String pattern = ((IRemoteControlEvent) record).getPattern();
		if (record instanceof DeactivationEvent) {
			this.monitoringController.deactivateProbe(pattern);
			this.monitoringController.clearPatternParameters(pattern);
		} else if (record instanceof ActivationParameterEvent) {
			final IRemoteParameterControlEvent event = (IRemoteParameterControlEvent) record;
			this.monitoringController.addPatternParameter(pattern, event.getName(),
					new LinkedList<>(Arrays.asList(event.getValues())));
			this.monitoringController.activateProbe(pattern);
		} else if (record instanceof ActivationEvent) {
			this.monitoringController.clearPatternParameters(pattern);
			this.monitoringController.activateProbe(pattern);
		} else if (record instanceof AddParameterValueEvent) {
			final AddParameterValueEvent event = (AddParameterValueEvent) record;
			this.monitoringController.addPatternParameterValue(pattern, event.getName(), event.getValue());
		} else if (record instanceof RemoveParameterValueEvent) {
			final RemoveParameterValueEvent event = (RemoveParameterValueEvent) record;
			this.monitoringController.removePatternParameterValue(pattern, event.getName(), event.getValue());
		} else if (record instanceof UpdateParameterEvent) {
			final IRemoteParameterControlEvent event = (IRemoteParameterControlEvent) record;
			this.monitoringController.addPatternParameter(pattern, event.getName(),
					new LinkedList<>(Arrays.asList(event.getValues())));
		} else {
			MonitoringCommandListener.LOGGER.info("Received unknown remote control event: {}",
					record.getClass().getName());
		}
	}
}
