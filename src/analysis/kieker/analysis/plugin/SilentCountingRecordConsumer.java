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
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

/**
 * 
 * @author Jan Waller
 */
public final class SilentCountingRecordConsumer extends AbstractAnalysisPlugin {

	private final AtomicLong counter = new AtomicLong();
	private final OutputPort output = new OutputPort("out", null);
	private final AbstractInputPort input = new AbstractInputPort("in", null) {
		@Override
		public void newEvent(final Object event) {
			SilentCountingRecordConsumer.this.counter.incrementAndGet();

			output.deliver(event);
		}
	};

	/**
	 * Constructs a {@link SilentCountingRecordConsumer}.
	 */
	public SilentCountingRecordConsumer(final Configuration configuration) {
		super(configuration);

		registerInputPort("in", input);
		registerOutputPort("out", output);
	}

	@Override
	public final boolean execute() {
		return true;
	}

	@Override
	public final void terminate(final boolean error) {
		// nothing to do
	}

	public final long getMessageCount() {
		return this.counter.get();
	}

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}
}
