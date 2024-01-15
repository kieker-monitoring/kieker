/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar.stages.calls;

import java.time.Duration;

import kieker.analysis.architecture.recovery.events.CallEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.code.data.CallerCalleeEntry;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Transform @{link CallerCallee}s to @{link OperationEvent}s and @{CallEvent}s on model level. The
 * stage outputs two @{link OperationEvent}s and one @{link CallEvent}s for each @{CallerCallee}
 * event. It is used to convert static caller-callee data to operation and call data compatible with
 * the dynamic architecture reconstruction.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class OperationAndCall4StaticDataStage extends AbstractConsumerStage<CallerCalleeEntry> {

	private final OutputPort<OperationEvent> operationOutputPort = this.createOutputPort(OperationEvent.class);
	private final OutputPort<CallEvent> callOutputPort = this.createOutputPort(CallEvent.class);

	private final String hostname;

	public OperationAndCall4StaticDataStage(final String hostname) {
		this.hostname = hostname;
	}

	@Override
	protected void execute(final CallerCalleeEntry element) throws Exception {
		final OperationEvent caller = new OperationEvent(this.hostname, element.getSourceModule(), element.getCaller());
		final OperationEvent callee = new OperationEvent(this.hostname, element.getTargetModule(), element.getCallee());

		final CallEvent callEvent = new CallEvent(caller, callee, Duration.ZERO);

		this.operationOutputPort.send(caller);
		this.operationOutputPort.send(callee);
		this.callOutputPort.send(callEvent);
	}

	public OutputPort<CallEvent> getCallOutputPort() {
		return this.callOutputPort;
	}

	public OutputPort<OperationEvent> getOperationOutputPort() {
		return this.operationOutputPort;
	}
}
