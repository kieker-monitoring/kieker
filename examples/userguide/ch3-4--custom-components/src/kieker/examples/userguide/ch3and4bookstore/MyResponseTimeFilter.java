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

import java.util.concurrent.TimeUnit;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

@Plugin(
		name = "Response Time filter",
		description = "Filters incoming response times based on a threshold",
		outputPorts = {
			@OutputPort(name = MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_VALID,
					description = "Outputs response times satisfying the threshold",
					eventTypes = { MyResponseTimeRecord.class }),
			@OutputPort(name = MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_EXCEED,
					description = "Outputs response times exceeding the threshold",
					eventTypes = { MyResponseTimeRecord.class }) })
public class MyResponseTimeFilter extends AbstractFilterPlugin {
	public static final String OUTPUT_PORT_NAME_RT_VALID = "validResponseTimes";
	public static final String OUTPUT_PORT_NAME_RT_EXCEED = "invalidResponseTimes";

	public static final String CONFIG_PROPERTY_NAME_RT_TS_NANOS = "rtThresholdNanos";

	private final long rtThresholdNanos; // the configure threshold for this filter instance

	public MyResponseTimeFilter(final Configuration configuration) {
		super(configuration);
		this.rtThresholdNanos =
				configuration.getLongProperty(CONFIG_PROPERTY_NAME_RT_TS_NANOS);
	}

	public static final String INPUT_PORT_NAME_RESPONSE_TIMES = "newResponseTime";

	@InputPort(
			name = MyResponseTimeFilter.INPUT_PORT_NAME_RESPONSE_TIMES,
			description = "Filter the given record depending on the response time",
			eventTypes = { MyResponseTimeRecord.class })
	public void newResponseTime(final MyResponseTimeRecord rtRecord) {
		if (rtRecord.responseTimeNanos > this.rtThresholdNanos) {
			super.deliver(OUTPUT_PORT_NAME_RT_EXCEED, rtRecord);
		} else {
			super.deliver(OUTPUT_PORT_NAME_RT_VALID, rtRecord);
		}
	}

	public static final long CONFIG_PROPERTY_VALUE_RT_TS_NANOS_DEFAULT =
			TimeUnit.NANOSECONDS.convert(1l, TimeUnit.MILLISECONDS);

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_RT_TS_NANOS,
				Long.toString(CONFIG_PROPERTY_VALUE_RT_TS_NANOS_DEFAULT));
		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_RT_TS_NANOS, Long.toString(this.rtThresholdNanos));
		return configuration;
	}
}
