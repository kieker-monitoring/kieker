/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.plugin;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.plugin.port.AInputPort;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.common.configuration.Configuration;

/**
 * This class has exactly one input port and one output port. An instance of this class receives any objects, increments an intern tread-safe counter without
 * printing any message and delivers the objects unmodified to the output. The value of the counter can later be retrieved by using a corresponding method.
 * 
 * @author Jan Waller
 */
@APlugin(outputPorts = {
	@AOutputPort(name = SilentCountingRecordConsumer.OUTPUT_PORT_NAME, eventTypes = {}, description = "Default output port")
})
public final class SilentCountingRecordConsumer extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT_NAME = "newEvent";

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Constructs a {@link SilentCountingRecordConsumer}.
	 */
	public SilentCountingRecordConsumer(final Configuration configuration) {
		super(configuration);
	}

	@AInputPort(eventTypes = {}, description = "Default input port")
	public final void newEvent(final Object event) {
		SilentCountingRecordConsumer.this.counter.incrementAndGet();
		super.deliver(SilentCountingRecordConsumer.OUTPUT_PORT_NAME, event);
	}

	public final long getMessageCount() {
		return this.counter.get();
	}

	@Override
	public final boolean execute() {
		return true;
	}

	@Override
	public final void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
