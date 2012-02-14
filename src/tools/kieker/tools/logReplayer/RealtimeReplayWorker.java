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

package kieker.tools.logReplayer;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * A Runnable to be scheduled via the RealtimeReplayDistributor
 * 
 * @author Robert von Massow
 * 
 */
@Plugin(outputPorts = {
	@OutputPort(name = RealtimeReplayWorker.OUTPUT_PORT_NAME, eventTypes = { IMonitoringRecord.class })
})
public class RealtimeReplayWorker extends AbstractAnalysisPlugin implements Runnable {
	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	private static final Log LOG = LogFactory.getLog(RealtimeReplayWorker.class);
	private final IMonitoringRecord monRec;
	private final RealtimeReplayDistributor rd;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param monRec
	 *            The record to be send to the plugin.
	 * @param rd
	 *            The distributor.
	 * @param cons
	 *            The plugin which receives the record. The plugin <b>must</b> have at least one input port. The data will be send to the first input.
	 */
	public RealtimeReplayWorker(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd, final AbstractAnalysisPlugin cons,
			final String constInputPortName) {
		super(new Configuration());
		this.monRec = monRec;
		this.rd = rd;

		AbstractPlugin.connect(this, RealtimeReplayWorker.OUTPUT_PORT_NAME, cons, constInputPortName);
	}

	public RealtimeReplayWorker() {
		super(new Configuration());
		this.monRec = null;
		this.rd = null;
	}

	@Override
	public void run() {
		if (this.monRec != null) {
			if (!super.deliver(RealtimeReplayWorker.OUTPUT_PORT_NAME, this.monRec)) {
				// TODO: check what to do
				// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/145
				RealtimeReplayWorker.LOG.error("Consumer returned with error");
			}
			this.rd.decreaseActive();
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		// TODO Default Configuration
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		// TODO Current Configuration
		return new Configuration();
	}
}
