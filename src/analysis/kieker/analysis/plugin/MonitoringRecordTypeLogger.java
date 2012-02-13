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

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port and one output port. An instance of this class receives only objects implementing the interface {@link IMonitoringRecord},
 * prints a log message for every single received record and passes them unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@Plugin(outputPorts = {
	@OutputPort(
			name = MonitoringRecordTypeLogger.OUTPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class },
			description = "Default output port")
})
public class MonitoringRecordTypeLogger extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT_NAME = "newEvent";

	private static final Log LOG = LogFactory.getLog(MonitoringRecordTypeLogger.class);

	@InputPort(
			name = MonitoringRecordTypeLogger.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class },
			description = "Default input port")
	public void newEvent(final Object event) {
		final IMonitoringRecord monitoringRecord = (IMonitoringRecord) event;

		/* Print a simple message to the output stream */
		MonitoringRecordTypeLogger.LOG.info("Consumed record:" + monitoringRecord.getClass().getName());
		MonitoringRecordTypeLogger.LOG.info(monitoringRecord.toString());

		/* Delegate the monitoring record. */
		super.deliver(MonitoringRecordTypeLogger.OUTPUT_PORT_NAME, event);
	}

	/**
	 * Constructs a {@link MonitoringRecordTypeLogger}.
	 */
	public MonitoringRecordTypeLogger(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
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

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
