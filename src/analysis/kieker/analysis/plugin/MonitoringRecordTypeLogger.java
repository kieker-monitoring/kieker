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

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port named "in" and one output ports named
 * "out".
 * 
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeLogger extends AbstractAnalysisPlugin {

	private static final Log LOG = LogFactory.getLog(MonitoringRecordTypeLogger.class);
	private final OutputPort output = new OutputPort("out", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class })));
	private final AbstractInputPort input = new AbstractInputPort("in", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }))) {
		@Override
		public void newEvent(final Object event) {
			IMonitoringRecord monitoringRecord = (IMonitoringRecord) event;

			MonitoringRecordTypeLogger.LOG.info("Consumed record:" + monitoringRecord.getClass().getName());
			MonitoringRecordTypeLogger.LOG.info(monitoringRecord.toString());

			output.deliver(event);
		}
	};

	/**
	 * Constructs a {@link MonitoringRecordTypeLogger}.
	 */
	public MonitoringRecordTypeLogger(final Configuration configuration) {
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

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}
}
