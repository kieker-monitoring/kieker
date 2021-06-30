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
package kieker.analysis.stage.general;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Send an incoming event of type Base to the output, if and only if a suitable Control event was received.
 *
 * @param <C>
 *            control event type
 * @param <B>
 *            base event type to be controlled by control events
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class ControlledEventReleaseStage<C, B> extends AbstractStage {

	private final InputPort<C> controlInputPort = this.createInputPort();
	private final InputPort<B> baseInputPort = this.createInputPort();

	private final OutputPort<B> outputPort = this.createOutputPort();

	private final List<B> storedBaseEvents = new ArrayList<>();
	private final List<C> storedControlEvents = new ArrayList<>();

	private final IControlEventMatcher<C, B> matcher;

	public ControlledEventReleaseStage(final IControlEventMatcher<C, B> matcher) {
		this.matcher = matcher;
	}

	@Override
	protected void execute() throws Exception {
		final C control = this.controlInputPort.receive();
		if (control != null) {
			this.stackControlOrCheckBase(control);
		}
		final B base = this.baseInputPort.receive();
		if (base != null) {
			this.storeOrRelease(base);
		}
	}

	private void stackControlOrCheckBase(final C control) {
		for (final B element : this.storedBaseEvents) {
			if (this.matcher.checkControlEvent(control, element)) {
				this.storedBaseEvents.remove(element);
				this.outputPort.send(element);
				return;
			}
		}

		this.storedControlEvents.add(control);
	}

	private void storeOrRelease(final B base) {
		if (this.matcher.requiresControlEvent(base)) {
			for (final C control : this.storedControlEvents) {
				if (this.matcher.checkControlEvent(control, base)) {
					if (!this.matcher.keepControlEvent(base)) {
						this.storedControlEvents.remove(control);
					}
					this.outputPort.send(base);
					return;
				}
			}
		} else {
			this.outputPort.send(base);
		}
	}

	public InputPort<C> getControlInputPort() {
		return this.controlInputPort;
	}

	public InputPort<B> getBaseInputPort() {
		return this.baseInputPort;
	}

	public OutputPort<B> getOutputPort() {
		return this.outputPort;
	}
}
