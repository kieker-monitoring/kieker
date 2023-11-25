/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.filter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.StorableDetectionResult;

/**
 * This filter provides a simply visualization for the OPAD anomaly detection filter.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 * @deprecated 1.15.1
 */
@Deprecated
@Plugin(configuration = {
	@Property(name = AnomalyScoreVisualizationFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
			defaultValue = AnomalyScoreVisualizationFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES) })
public class AnomalyScoreVisualizationFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private final int numberOfEntries;

	public AnomalyScoreVisualizationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
	}

	@InputPort(name = AnomalyScoreVisualizationFilter.INPUT_PORT_NAME_EVENTS, eventTypes = StorableDetectionResult.class)
	public void inputNormal(final StorableDetectionResult record) {
		this.updateDisplays(record);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));

		return configuration;
	}

	private void updateDisplays(final StorableDetectionResult record) {
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getTimestamp(), super.recordsTimeUnitFromProjectContext));
		date.toString().substring(14, 19);
	}

}
