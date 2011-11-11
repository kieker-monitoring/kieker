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
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * A Runnable to be scheduled via the RealtimeReplayDistributor
 * 
 * @author Robert von Massow
 * 
 */
public class RealtimeReplayWorker implements Runnable {

	private static final Log LOG = LogFactory.getLog(RealtimeReplayWorker.class);
	private final IMonitoringRecord monRec;
	private final RealtimeReplayDistributor rd;
	private final OutputPort output = new OutputPort("out", null);

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param monRec
	 *            The record to be send to the plugin.
	 * @param rd
	 *            The distributor.
	 * @param cons
	 *            The plugin which receives the record. The plugin <b>must</b> have at least one input port.
	 * @param inputPort
	 *            The name of the input port of the plugin to which the monitoring record will be send.
	 */
	public RealtimeReplayWorker(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd, final AbstractAnalysisPlugin cons, final String inputPort) {
		this.monRec = monRec;
		this.rd = rd;

		output.subscribe(cons.getInputPort(inputPort));
	}

	@Override
	public void run() {
		if (this.monRec != null) {
			if (!output.deliver(this.monRec)) {
				// TODO: check what to do
				// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/145
				RealtimeReplayWorker.LOG.error("Consumer returned with error");
			}
			this.rd.decreaseActive();
		}
	}
}
