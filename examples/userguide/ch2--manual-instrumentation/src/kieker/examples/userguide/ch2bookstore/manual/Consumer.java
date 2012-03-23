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

package kieker.examples.userguide.ch2bookstore.manual;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;

public class Consumer extends AbstractFilterPlugin {
	private final long maxResponseTime;
	public static final String INPUT_PORT_NAME = "newEvent";
	public static final String CONFIG_MAX_RESPONSE_TIME = "maxResponseTime";

	public Consumer(final Configuration configuration) {
		super(configuration);
		this.maxResponseTime = configuration.getLongProperty(Consumer.CONFIG_MAX_RESPONSE_TIME);
	}

	@InputPort(
			name = Consumer.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class })
	public void newEvent(final Object event) {
		if (!(event instanceof OperationExecutionRecord)) {
			return;
		}
		final OperationExecutionRecord rec = (OperationExecutionRecord) event;
		/* Derive response time from the record. */
		final long responseTime = rec.getTout() - rec.getTin();
		/* Now compare with the response time threshold: */
		if (responseTime > this.maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - this.maxResponseTime) + " ns: " + rec.getOperationSignature());
		} else {
			System.out.println("response time accepted: " + rec.getOperationSignature());
		}
		return;
	}

	@Override
	public Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
