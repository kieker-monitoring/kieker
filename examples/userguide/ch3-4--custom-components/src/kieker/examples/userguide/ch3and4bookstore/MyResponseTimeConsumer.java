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

import java.util.HashMap;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

public class MyResponseTimeConsumer extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newEvent";

	public MyResponseTimeConsumer(final Configuration configuration, final HashMap<String, AbstractRepository> repositories) {
		super(configuration, repositories);
	}

	public MyResponseTimeConsumer() {
		super(new Configuration(null), new HashMap<String, AbstractRepository>());
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
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}

	@Override
	public HashMap<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
