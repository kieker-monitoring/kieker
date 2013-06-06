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
import kieker.common.record.system.CPUUtilizationRecord;

/**
 * This is a filter which accepts {@link CPUUtilizationRecord} instances and provides different views to visualize them. The incoming records are relayed without any
 * enrichment.
 * 
 * @author Bjoern Weissenfels, Nils Ehmke
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
				description = "sets the number of max plot entries per cpu"))
public class CPUUtilizationDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private static final String TOTAL_UTILIZATION = "totalUtilization";
	private static final String ILDE = "idle";
	private static final String IRQ = "irq";
	private static final String NICE = "nice";
	private static final String SYSTEM = "system";
	private static final String USER = "user";

	private final MeterGauge meterGauge;
	private final XYPlot xyplot;
	private final int numberOfEntries;

	public CPUUtilizationDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Create the display objects
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		this.meterGauge = new MeterGauge();
		this.xyplot = new XYPlot(this.numberOfEntries);

	}

	@InputPort(name = CPUUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { CPUUtilizationRecord.class })
	public void input(final CPUUtilizationRecord record) {
		final String id = record.getHostname() + " - " + record.getCpuID();

		this.meterGauge.setIntervals(id, Arrays.asList((Number) 70, 90, 100), Arrays.asList("66cc66", "E7E658", "cc6666"));
		this.meterGauge.setValue(id, record.getTotalUtilization() * 100);

		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.TOTAL_UTILIZATION, record.getLoggingTimestamp(), record.getTotalUtilization() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.ILDE, record.getLoggingTimestamp(), record.getIdle() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.IRQ, record.getLoggingTimestamp(), record.getIrq() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.NICE, record.getLoggingTimestamp(), record.getNice() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.SYSTEM, record.getLoggingTimestamp(), record.getSystem() * 100);
		this.xyplot.setEntry(id + " - " + CPUUtilizationDisplayFilter.USER, record.getLoggingTimestamp(), record.getUser() * 100);

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
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
