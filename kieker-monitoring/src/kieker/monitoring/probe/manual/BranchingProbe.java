/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.manual;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.BranchingRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * Convenience class which provides a static method to log branching.
 * 
 * @author Andre van Hoorn
 * 
 * @since 0.95a
 */
public final class BranchingProbe implements IMonitoringProbe {
	private static final Log LOG = LogFactory.getLog(BranchingProbe.class);
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIMESOURCE = CTRLINST.getTimeSource();

	private BranchingProbe() {}

	public static final void monitorBranch(final int branchID, final int branchingOutcome) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		// try-catch in order to avoid that any exception is propagated to the application code.
		try {
			CTRLINST.newMonitoringRecord(new BranchingRecord(TIMESOURCE.getTime(), branchID, branchingOutcome));
		} catch (final Exception ex) { // NOPMD NOCS (Exceptions)
			LOG.error("Error monitoring branching", ex);
		}
	}
}
