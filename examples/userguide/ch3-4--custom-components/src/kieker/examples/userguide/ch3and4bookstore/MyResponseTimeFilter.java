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
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

@Plugin(outputPorts = {
	@OutputPort(name = MyResponseTimeFilter.OUTPUT_PORT_NAME_VALID_EVENTS, eventTypes = { MyResponseTimeRecord.class }),
	@OutputPort(name = MyResponseTimeFilter.OUTPUT_PORT_NAME_INVALID_EVENTS, eventTypes = { MyResponseTimeRecord.class }) })
public class MyResponseTimeFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "newEvent";
	public static final String OUTPUT_PORT_NAME_VALID_EVENTS = "validEvent";
	public static final String OUTPUT_PORT_NAME_INVALID_EVENTS = "invalidEvent";
	public static final String CONFIG_PROPERTY_NAME_MAX_RESPONSE_TIME = "maxResponseTime";

	private final long maxResponseTime;

	public MyResponseTimeFilter(final Configuration configuration) {
		super(configuration);
		this.maxResponseTime = configuration.getLongProperty(MyResponseTimeFilter.CONFIG_PROPERTY_NAME_MAX_RESPONSE_TIME);
	}

	@InputPort(name = MyResponseTimeFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { MyResponseTimeRecord.class })
	public void newEvent(final Object event) {
		if (event instanceof MyResponseTimeRecord) {
			/* Filter the given record depending on the actual response time. */
			final MyResponseTimeRecord myRecord = (MyResponseTimeRecord) event;

			if (myRecord.responseTimeNanos > this.maxResponseTime) {
				super.deliver(MyResponseTimeFilter.OUTPUT_PORT_NAME_INVALID_EVENTS, event);
			} else {
				super.deliver(MyResponseTimeFilter.OUTPUT_PORT_NAME_VALID_EVENTS, event);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(MyResponseTimeFilter.CONFIG_PROPERTY_NAME_MAX_RESPONSE_TIME, Long.toString(1000000));

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(MyResponseTimeFilter.CONFIG_PROPERTY_NAME_MAX_RESPONSE_TIME, Long.toString(this.maxResponseTime));

		return configuration;
	}
}
