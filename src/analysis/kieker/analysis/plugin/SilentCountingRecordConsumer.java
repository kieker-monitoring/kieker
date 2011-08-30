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

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Jan Waller
 */
public final class SilentCountingRecordConsumer implements IMonitoringRecordConsumerPlugin {

	private AtomicLong counter = new AtomicLong();

	public final Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null;
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		counter.incrementAndGet();
		return true;
	}

	public final boolean execute() {
		return true;
	}

	public final void terminate(final boolean error) {
	}

	public final long getMessageCount() {
		return counter.get();
	}
}
