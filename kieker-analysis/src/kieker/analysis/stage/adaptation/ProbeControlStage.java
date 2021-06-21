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
package kieker.analysis.stage.adaptation;

import kieker.analysis.stage.adaptation.data.Alarms;
import kieker.analysis.stage.adaptation.data.IErrorMessages;
import kieker.analysis.stage.adaptation.events.AbstractTcpControlEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Receives {@link AbstractTcpControlEvent control events}, processes them and in case of error
 * sends the error message to an error sink.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public class ProbeControlStage extends AbstractConsumerStage<AbstractTcpControlEvent> {
	private final IProbeController probeController;
	private final OutputPort<IErrorMessages> outputPort = this.createOutputPort();

	/**
	 * Initiates the filter with a new {@link TcpProbeController}.
	 *
	 * @param probeController
	 *            corresponding probe controller
	 */
	public ProbeControlStage(final IProbeController probeController) {
		this.probeController = probeController;
	}

	@Override
	protected void execute(final AbstractTcpControlEvent event) {
		try {
			this.probeController.controlProbe(event);
		} catch (final RemoteControlFailedException e) {
			final String alarmMessage = "Could not send probe control event " + e.getMessage();
			final Alarms alarms = new Alarms();
			alarms.addMessage(alarmMessage);
			this.outputPort.send(alarms);
		}
	}

	public OutputPort<IErrorMessages> getOutputPort() {
		return this.outputPort;
	}

}
