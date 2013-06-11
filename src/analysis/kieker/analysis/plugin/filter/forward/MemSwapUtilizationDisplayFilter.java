/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.forward;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.PieChart;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.system.MemSwapUsageRecord;

/**
 * This is a filter which accepts {@link MemSwapUsageRecord} instances and provides different views to visualize them. The incoming records are relayed without any
 * enrichment.
 * 
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = MemSwapUtilizationDisplayFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				eventTypes = { MemSwapUsageRecord.class },
				description = "Provides each incoming object"),
		configuration =
		@Property(
				name = MemSwapUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				defaultValue = MemSwapUtilizationDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
				description = "Sets the number of max plot entries per record entry"))
public class MemSwapUtilizationDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private static final Log LOG = LogFactory.getLog(MemSwapUtilizationDisplayFilter.class);

	private static final String MEM_FREE = "memFree";
	private static final String MEM_TOTAL = "memTotal";
	private static final String MEM_USED = "memUsed";
	private static final String SWAP_FREE = "swapFree";
	private static final String SWAP_TOTAL = "swapTotal";
	private static final String SWAP_USED = "swapUsed";

	private final XYPlot xyplot;
	private final PieChart memPieChart;
	private final PieChart swapPieChart;

	private final int numberOfEntries;

	private final TimeUnit timeunit;

	public MemSwapUtilizationDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Read the configuration
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);

		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;

		// Create the display objects
		this.xyplot = new XYPlot(this.numberOfEntries);
		this.memPieChart = new PieChart();
		this.swapPieChart = new PieChart();
	}

	@InputPort(name = MemSwapUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { MemSwapUsageRecord.class })
	public void input(final MemSwapUsageRecord record) {
		this.updateDisplays(record);

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}

	private void updateDisplays(final MemSwapUsageRecord record) {
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.timeunit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = record.getHostname();

		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE, minutesAndSeconds, record.getMemFree() * 100);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_TOTAL, minutesAndSeconds, record.getMemTotal() * 100);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED, minutesAndSeconds, record.getMemUsed() * 100);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE, minutesAndSeconds, record.getSwapFree() * 100);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_TOTAL, minutesAndSeconds, record.getSwapTotal() * 100);
		this.xyplot.setEntry(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED, minutesAndSeconds, record.getSwapUsed() * 100);

		this.memPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE, record.getMemFree());
		this.memPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED, record.getMemUsed());

		this.swapPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE, record.getSwapFree());
		this.swapPieChart.setValue(id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED, record.getSwapUsed());
	}

	@Display(name = "XYPlot Memory utilization Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

	@Display(name = "PieChart Memory Utilization Display")
	public PieChart getMemPieChart() {
		return this.memPieChart;
	}

	@Display(name = "PieChart Swap Utilization Display")
	public PieChart getSwapPieChart() {
		return this.swapPieChart;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));

		return configuration;
	}

}
