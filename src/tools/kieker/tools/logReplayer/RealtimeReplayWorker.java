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

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
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
	// private static final Log LOG = LogFactory.getLog(RealtimeReplayWorker.class);
	private IMonitoringRecord monRec;
	private RealtimeReplayDistributor rd;

	/**
	 * Creates a new instance of this class using the given parameters. Further initialization should be done via the <i>initialize</i>-method.
	 * 
	 * @param configuration
	 *            The configuration object used to configure this instance.
	 */
	public RealtimeReplayWorker(final Configuration configuration) {
		super(configuration);
	}

	public void initialize(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd,
			final AbstractAnalysisPlugin cons,
			final String constInputPortName, final AnalysisController controller) {
		this.monRec = monRec;
		this.rd = rd;

		controller.connect(this, RealtimeReplayWorker.OUTPUT_PORT_NAME, cons, constInputPortName);
	}

	@Override
	public void run() {
		if (this.monRec != null) {
			super.deliver(RealtimeReplayWorker.OUTPUT_PORT_NAME, this.monRec);
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
