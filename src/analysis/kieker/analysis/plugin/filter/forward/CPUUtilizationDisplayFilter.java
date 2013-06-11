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

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.MeterGauge;
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
import kieker.common.record.system.CPUUtilizationRecord;

/**
 * This is a filter which accepts {@link CPUUtilizationRecord} instances and provides different views to visualize them. The incoming records are relayed without any
 * enrichment.
 * 
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = CPUUtilizationDisplayFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				eventTypes = { CPUUtilizationRecord.class },
				description = "Provides each incoming object"),
		configuration =
		@Property(
				name = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				defaultValue = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
				description = "Sets the number of max plot entries per cpu"))
public class CPUUtilizationDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private static final Log LOG = LogFactory.getLog(CPUUtilizationDisplayFilter.class);

	private static final String TOTAL_UTILIZATION = "totalUtilization";
	private static final String IDLE = "idle";
	private static final String IRQ = "irq";
	private static final String NICE = "nice";
	private static final String SYSTEM = "system";
	private static final String USER = "user";

	private final MeterGauge meterGauge;
	private final XYPlot xyplot;
	private final int numberOfEntries;

	private final TimeUnit timeunit;

	public CPUUtilizationDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Create the display objects
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		this.meterGauge = new MeterGauge();
		this.xyplot = new XYPlot(this.numberOfEntries);

		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;
	}

	@InputPort(name = CPUUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { CPUUtilizationRecord.class })
	public void input(final CPUUtilizationRecord record) {
		this.updateDisplays(record);

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}

	private void updateDisplays(final CPUUtilizationRecord record) {
		synchronized (this) {

			// Calculate the minutes and seconds of the logging timestamp of the record
			final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.timeunit));
			final String minutesAndSeconds = date.toString().substring(14, 19);

			final String id = record.getHostname() + " - " + record.getCpuID();

			this.meterGauge.setIntervals(id, Arrays.asList((Number) 70, 90, 100), Arrays.asList("66cc66", "E7E658", "cc6666"));
			this.meterGauge.setValue(id, record.getTotalUtilization() * 100);

			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.TOTAL_UTILIZATION, minutesAndSeconds, record.getTotalUtilization() * 100);
			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.IDLE, minutesAndSeconds, record.getIdle() * 100);
			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.IRQ, minutesAndSeconds, record.getIrq() * 100);
			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.NICE, minutesAndSeconds, record.getNice() * 100);
			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.SYSTEM, minutesAndSeconds, record.getSystem() * 100);
			this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.USER, minutesAndSeconds, record.getUser() * 100);
		}
	}

	@Display(name = "Meter Gauge CPU total utilization Display")
	public MeterGauge getMeterGauge() {
		return this.meterGauge;
	}

	@Display(name = "XYPlot CPU utilization Display")
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
