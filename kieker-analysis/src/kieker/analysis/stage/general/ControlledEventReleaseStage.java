/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(ControlledEventReleaseStage.class);

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
		LOGGER.info("SCOCB stack control or check base {}", control.toString());
		for (final B element : this.storedBaseEvents) {
			if (this.matcher.checkControlEvent(control, element)) {
				this.storedBaseEvents.remove(element);
				this.outputPort.send(element);
				LOGGER.info("SCOCB PASS {}", element.toString());
				return;
			}
		}

		LOGGER.info("SCOCB STACK control {}", control.toString());
		this.storedControlEvents.add(control);
	}

	private void storeOrRelease(final B base) {
		LOGGER.info("SOR store or release {}", base.toString());
		if (this.matcher.requiresControlEvent(base)) {
			for (final C control : this.storedControlEvents) {
				if (this.matcher.checkControlEvent(control, base)) {
					if (!this.matcher.keepControlEvent(base)) {
						this.storedControlEvents.remove(control);
						LOGGER.info("SOR REMOVE control {}", control.toString());
					} else {
						LOGGER.info("SOR KEEP control {}", control.toString());
					}
					this.outputPort.send(base);
					LOGGER.info("SOR RELEASE base", base.toString());
					return;
				}
			}
		} else {
			LOGGER.info("SOR PASS without control", base.toString());
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
