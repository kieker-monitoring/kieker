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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

public class MyResponseTimeConsumer extends AbstractFilterPlugin {
	public static final String INPUT_PORT_NAME = "newEvent";

	public MyResponseTimeConsumer(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(
			name = MyResponseTimeConsumer.INPUT_PORT_NAME,
			eventTypes = { MyResponseTimeRecord.class })
	public void newEvent(final Object event) {
		if (event instanceof MyResponseTimeRecord) {
			/* Write the content to the standard output stream. */
			final MyResponseTimeRecord myRecord = (MyResponseTimeRecord) event;
			System.out.println("[Consumer] " + myRecord.getLoggingTimestamp()
					+ ": " + myRecord.className + ", " + myRecord.methodName
					+ ", " + myRecord.responseTimeNanos);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}
}
