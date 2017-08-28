/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import kieker.analysis.display.PlainText;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * An instance of this class computes the throughput in terms of the number of objects received per time unit.
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
@Plugin(description = "A filter computing the throughput of the analysis",
		outputPorts = {
			@OutputPort(name = AnalysisThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS, eventTypes = { Object.class },
					description = "Provides each incoming object"),
			@OutputPort(name = AnalysisThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT, eventTypes = { Long.class },
					description = "Provides throughput since last timer event object")
		})
public class AnalysisThroughputFilter extends AbstractFilterPlugin {

	/** The name of the input port receiving other objects. */
	public static final String INPUT_PORT_NAME_OBJECTS = "inputObjects";
	/** The name of the input port receiving timestamps. */
	public static final String INPUT_PORT_NAME_TIME = "inputTime";
	/** The name of the output port delivering the received objects. */
	public static final String OUTPUT_PORT_NAME_RELAYED_OBJECTS = "relayedEvents";
	/** The name of the output port delivering the received objects. */
	public static final String OUTPUT_PORT_NAME_THROUGHPUT = "throughput";

	private final AtomicLong counter = new AtomicLong();

	private final PlainText plainTextDisplayObject = new PlainText();
	private volatile long lastTimestamp;

	public AnalysisThroughputFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = INPUT_PORT_NAME_OBJECTS, eventTypes = { Object.class }, description = "Receives incoming objects to calculate the throughput")
	public final void inputObjects(final Object object) {
		this.counter.incrementAndGet();
		super.deliver(OUTPUT_PORT_NAME_RELAYED_OBJECTS, object);
	}

	@InputPort(name = INPUT_PORT_NAME_TIME, eventTypes = { Long.class }, description = "Receives timestamps to calculate the throughput")
	public final void inputTime(final Long timestamp) {
		final long count = this.counter.getAndSet(0);
		final long duration = timestamp - this.lastTimestamp;
		final StringBuilder sb = new StringBuilder(256);
		sb.append(count);
		sb.append(" objects within ");
		sb.append(duration);
		sb.append(' ');
		sb.append(super.recordsTimeUnitFromProjectContext.toString());
		this.plainTextDisplayObject.setText(sb.toString());
		super.deliver(OUTPUT_PORT_NAME_THROUGHPUT, count);
		this.lastTimestamp = timestamp;
	}

	@Display(name = "Throughput Display")
	public final PlainText plainTextDisplay() {
		return this.plainTextDisplayObject;
	}
}
