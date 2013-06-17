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

package kieker.analysis.plugin.filter.forward;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.port.InputPort;
import kieker.analysis.port.OutputPort;
import kieker.common.configuration.Configuration;

/**
 * An instance of this class receives any objects, increments an intern tread-safe counter without printing any message and delivers the
 * unchanged objects to the output. The value of the counter can be retrieved by connected to the respective output port using a
 * corresponding method.
 * 
 * @author Jan Waller
 * 
 * @since 1.4
 */
@Plugin(description = "A filter counting the elements flowing through this filter")
public final class CountingFilter extends AbstractFilterPlugin {

	private final AtomicLong counter = new AtomicLong();

	private volatile long timeStampOfInitialization;

	private final OutputPort currentEventCountOutputPort;
	private final OutputPort relayedEventsOutputPort;
	private final InputPort incomingEventsInputPort;

	public CountingFilter(final Configuration configuration, final IProjectContext projectContext) throws AnalysisConfigurationException {
		super(configuration, projectContext);

		this.relayedEventsOutputPort = new OutputPort(new Class<?>[] { Object.class });
		this.currentEventCountOutputPort = new OutputPort(new Class<?>[] { Long.class });
		this.incomingEventsInputPort = new InputPort(new Class<?>[] { Object.class }, this, "inputEvent");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Returns the number of objects received until now.
	 * 
	 * @return The current counter value.
	 */
	public final long getMessageCount() {
		return this.counter.get();
	}

	/**
	 * This method represents the input port of this filter.
	 * 
	 * @param event
	 *            The next event.
	 */
	public final void inputEvent(final Object event) {
		final Long count = CountingFilter.this.counter.incrementAndGet();

		this.relayedEventsOutputPort.deliver(event);
		this.currentEventCountOutputPort.deliver(count);
	}

	@kieker.analysis.plugin.annotation.OutputPort
	public final OutputPort getRelayedEventsOutputPort() {
		return this.relayedEventsOutputPort;
	}

	@kieker.analysis.plugin.annotation.InputPort
	public final InputPort getIncomingEventsInputPort() {
		return this.incomingEventsInputPort;
	}

	@Override
	public boolean init() {
		if (super.init()) {
			this.timeStampOfInitialization = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

}
