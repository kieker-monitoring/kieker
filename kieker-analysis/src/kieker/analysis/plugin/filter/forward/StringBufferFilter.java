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

package kieker.analysis.plugin.filter.forward;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.util.KiekerHashMap;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * This filter has exactly one input port and one output port.
 *
 * Every record received is cloned and each detected String is buffered in a
 * shared area in order to save memory.
 *
 * @author Jan Waller
 *
 * @since 1.6
 *
 * @deprecated since 1.15 should be removed in 1.16 does not work anyway
 */
@Deprecated
@Plugin(description = "A filter to reduce the memory footprint of strings used in records",
		outputPorts = @OutputPort(name = StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object",
				eventTypes = Object.class))
public final class StringBufferFilter extends AbstractFilterPlugin {

	/** The name of the input port for the incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "received-events";
	/** The name of the output port for the relayed events. */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayed-events";

	private final KiekerHashMap kiekerHashMap = new KiekerHashMap();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public StringBufferFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@SuppressWarnings("unchecked")
	@InputPort(name = StringBufferFilter.INPUT_PORT_NAME_EVENTS, description = "Receives incoming objects to be buffered and forwarded",
			eventTypes = Object.class)
	public final void inputEvent(final Object object) {
		if (object instanceof String) {
			super.deliver(StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, this.kiekerHashMap.get((String) object));
		} else if (object instanceof IMonitoringRecord) {
			// final Object[] objects = ((IMonitoringRecord) object).toArray();
			// boolean stringBuffered = false;
			// for (int i = 0; i < objects.length; i++) {
			// if (objects[i] instanceof String) {
			// objects[i] = this.kiekerHashMap.get((String) objects[i]);
			// stringBuffered = true;
			// }
			// }
			// if (stringBuffered) {
			// try {
			// final IMonitoringRecord newRecord = AbstractMonitoringRecord.createFromArray((Class<? extends IMonitoringRecord>) object.getClass(), objects);
			// newRecord.setLoggingTimestamp(((IMonitoringRecord) object).getLoggingTimestamp());
			final IMonitoringRecord newRecord = (IMonitoringRecord) object;
			super.deliver(StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, newRecord);
			// } catch (final MonitoringRecordException ex) {
			// this.logger.warn("Failed to recreate buffered monitoring record: {}", object.toString(), ex);
			// }
			// } else {
			// super.deliver(StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
			// }
		} else { // simply forward the object
			super.deliver(StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
		}
	}

}
