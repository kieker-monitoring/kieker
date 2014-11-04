/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.opad;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.StorableDetectionResult;

@Plugin
public final class AnomalyPrinter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";

	public AnomalyPrinter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = { StorableDetectionResult.class })
	public final void inputEvent(final StorableDetectionResult result) {
		System.out.println("Forecasted value: " + result.getForecast() + ", Actual value: " + result.getValue() + ", Anomaly score: " + result.getScore());
	}
}
