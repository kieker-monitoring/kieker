/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.GCRecord;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
@Plugin(configuration = @Property(
		name = GCDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
		defaultValue = GCDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
		description = "Sets the number of max plot entries per record entry"))
public class GCDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private static final String COLLECTION_TIME = "Collection Time";
	private static final String COLLECTION_COUNT = "Collection Count";

	private final XYPlot xyplot;

	private final int numberOfEntries;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this filter.
	 * @param projectContext
	 *            The project context for this filter.
	 */
	public GCDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Read the configuration
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);

		// Create the display objects
		this.xyplot = new XYPlot(this.numberOfEntries);
	}

	/**
	 * This method represents the input port receiving the incoming events.
	 *
	 * @param record
	 *            The record to display and relay.
	 */
	@InputPort(name = GCDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = GCRecord.class)
	public void input(final GCRecord record) {
		this.updateDisplays(record);
	}

	private void updateDisplays(final GCRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), super.recordsTimeUnitFromProjectContext));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname() + " - " + record.getVmName() + " - " + record.getGcName();

		this.xyplot.setEntry(id + " - " + GCDisplayFilter.COLLECTION_COUNT, minutesAndSeconds, record.getCollectionCount());
		this.xyplot.setEntry(id + " - " + GCDisplayFilter.COLLECTION_TIME, minutesAndSeconds, record.getCollectionTimeMS());
	}

	@Display(name = "XYPlot Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));

		return configuration;
	}

}
