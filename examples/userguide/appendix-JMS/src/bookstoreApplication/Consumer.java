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

package bookstoreApplication;

import java.util.Collection;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;

public class Consumer implements IMonitoringRecordConsumerPlugin {

	private final long maxResponseTime;

	public Consumer(final long maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null;
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (!(record instanceof OperationExecutionRecord)) {
			return true;
		}
		final OperationExecutionRecord rec = (OperationExecutionRecord) record;
		/* Derive response time from the record. */
		final long responseTime = rec.getTout() - rec.getTin();
		/* Now compare with the response responseTime threshold: */
		if (responseTime > this.maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - this.maxResponseTime) + " ns: " + rec.getClassName()
					+ "." + rec.getOperationName());
		} else {
			System.out.println("response time accepted: " + rec.getClassName()
					+ "." + rec.getOperationName());
		}
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

}
