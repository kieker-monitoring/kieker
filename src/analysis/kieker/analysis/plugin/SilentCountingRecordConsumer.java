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

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;

/**
 * This class has exactly one input port named "in" and one output ports named "out". An instance of this class receives any objects, increments an intern
 * tread-safe counter without printing any message and delivers the objects unmodified to the output. The value of the counter can later be retrieved by
 * using a corresponding method.
 * 
 * @author Jan Waller
 */
public final class SilentCountingRecordConsumer extends AbstractAnalysisPlugin implements ISingleInputPort {
	private final AtomicLong counter = new AtomicLong();
	private final OutputPort output = new OutputPort("out", null);
	private final InputPort input = new InputPort("in", null, this); // TODO: escaping this in constructor!

	/**
	 * Constructs a {@link SilentCountingRecordConsumer}.
	 */
	public SilentCountingRecordConsumer(final Configuration configuration) {
		super(configuration);

		/* Register the necessary ports. */
		this.registerInputPort("in", this.input);
		this.registerOutputPort("out", this.output);
	}

	@Override
	public final void newEvent(final Object event) {
		SilentCountingRecordConsumer.this.counter.incrementAndGet();
		SilentCountingRecordConsumer.this.output.deliver(event);
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
	protected Properties getDefaultProperties() {
		return new Properties();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}
}
