package kieker.monitoring.probe.manual;

import kieker.common.record.BranchingRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * Convenience class which provides a static method to log branching.
 * 
 * @author Andre van Hoorn
 */
public class BranchingProbe implements IMonitoringProbe {
	private static final Log log = LogFactory.getLog(BranchingProbe.class);
	private static final IMonitoringController ctrlInst = MonitoringController.getInstance();
	private static final ITimeSource timesource = ctrlInst.getTimeSource();

	public static void monitorBranch(final int branchID, final int branchingOutcome) {
		// try-catch in order to avoid that any exception is propagated to the application code.
		try {
			BranchingProbe.ctrlInst.newMonitoringRecord(new BranchingRecord(timesource.getTime(), branchID, branchingOutcome));
		} catch (final Exception ex) {
			BranchingProbe.log.error("Error monitoring branching", ex);
		}
	}
}
