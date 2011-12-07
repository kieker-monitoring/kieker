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

import kieker.analysis.plugin.port.AInputPort;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port named "in" and one output ports named
 * "out". An instance of this class receives only objects implementing the
 * interface {@link IMonitoringRecord}, prints a log message for every single
 * received record and passes them unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@APlugin(outputPorts = {
	@AOutputPort(name = MonitoringRecordTypeLogger.OUTPUT_PORT, eventTypes = { IMonitoringRecord.class })
})
public class MonitoringRecordTypeLogger extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT = "output";

	private static final Log LOG = LogFactory.getLog(MonitoringRecordTypeLogger.class);

	@AInputPort(eventTypes = { IMonitoringRecord.class })
	public void newEvent(final Object event) {
		final IMonitoringRecord monitoringRecord = (IMonitoringRecord) event;

		MonitoringRecordTypeLogger.LOG.info("Consumed record:" + monitoringRecord.getClass().getName());
		MonitoringRecordTypeLogger.LOG.info(monitoringRecord.toString());

		super.deliver(MonitoringRecordTypeLogger.OUTPUT_PORT, event);
	}

	/**
	 * Constructs a {@link MonitoringRecordTypeLogger}.
	 */
	public MonitoringRecordTypeLogger(final Configuration configuration) {
		super(configuration);
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
