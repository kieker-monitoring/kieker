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

package kieker.analysis.filter;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;

/**
 * This class has exactly one input port and one output port. An instance of this class receives any objects, increments an intern tread-safe counter without
 * printing any message and delivers the objects unmodified to the output. The value of the counter can later be retrieved by using a corresponding method.
 * 
 * @author Jan Waller
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME, eventTypes = {}, description = "all incoming objects are forwarded"),
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_COUNT, eventTypes = {}, description = "the current count of objects")
		})
public final class CountingFilter extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT_NAME = "output";
	public static final String OUTPUT_PORT_NAME_COUNT = "count";
	public static final String INPUT_PORT_NAME = "input";

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Constructs a {@link CountingFilter}.
	 */
	public CountingFilter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	public final long getMessageCount() {
		return this.counter.get();
	}

	@InputPort(name = CountingFilter.INPUT_PORT_NAME, eventTypes = {}, description = "incoming objects are counted and forwarded")
	public final void newEvent(final Object event) {
		final Long count = CountingFilter.this.counter.incrementAndGet();
		super.deliver(CountingFilter.OUTPUT_PORT_NAME, event);
		super.deliver(CountingFilter.OUTPUT_PORT_NAME_COUNT, count);
	}
}
