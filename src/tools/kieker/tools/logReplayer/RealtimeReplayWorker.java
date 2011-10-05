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

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Runnable to be scheduled via the RealtimeReplayDistributor
 * 
 * @author Robert von Massow
 * 
 */
public class RealtimeReplayWorker implements Runnable {

	private static final Log log = LogFactory.getLog(RealtimeReplayWorker.class);
	private final IMonitoringRecord monRec;
	private final IMonitoringRecordConsumerPlugin cons;
	private final RealtimeReplayDistributor rd;

	public RealtimeReplayWorker(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd, final IMonitoringRecordConsumerPlugin cons) {
		this.monRec = monRec;
		this.cons = cons;
		this.rd = rd;
	}

	@Override
	public void run() {
		if (this.monRec != null) {
			if (!this.cons.newMonitoringRecord(this.monRec)) {
				// TODO: check what to do
				// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/145
				RealtimeReplayWorker.log.error("Consumer returned with error");
			}
			this.rd.decreaseActive();
		}
	}
}
