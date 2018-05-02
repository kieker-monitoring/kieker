/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.record;

import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * An instance of this class computes the throughput in terms of the number of
 * records logged within the monitoring instance per time unit.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.8
 */
@Plugin(description = "A filter computing the throughput of the monitoring", outputPorts = {
	@OutputPort(name = MonitoringThroughputFilter.OUTPUT_PORT_NAME_RELAYED_RECORDS, eventTypes = {
		IMonitoringRecord.class }, description = "Provides each incoming record"),
	@OutputPort(name = MonitoringThroughputFilter.OUTPUT_PORT_NAME_UNCOUNTED_RECORDS, eventTypes = {
		IMonitoringRecord.class }, description = "Provides each not counted record"),
	@OutputPort(name = MonitoringThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT, eventTypes = {
		Long.class }, description = "Provides throughput within last interval") }, configuration = {
			@Property(name = MonitoringThroughputFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = MonitoringThroughputFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = MonitoringThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE, defaultValue = MonitoringThroughputFilter.CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_SECOND) })
public class MonitoringThroughputFilter extends AbstractFilterPlugin {

	/** The name of the input port receiving other objects. */
	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	/** The name of the output port delivering the received objects. */
	public static final String OUTPUT_PORT_NAME_RELAYED_RECORDS = "relayedRecords";
	/** The name of the output port delivering the uncounted objects. */
	public static final String OUTPUT_PORT_NAME_UNCOUNTED_RECORDS = "uncountedRecords";
	/** The name of the output port delivering calculated throughput. */
	public static final String OUTPUT_PORT_NAME_THROUGHPUT = "throughput";

	/** The name of the property determining the time unit. */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	/** The default value of the time unit property (nanoseconds). */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "SECONDS"; // TimeUnit.NANOSECONDS.name()
	/** The name of the property determining the interval size. */
	public static final String CONFIG_PROPERTY_NAME_INTERVAL_SIZE = "intervalsize";
	/**
	 * The configuration property value for
	 * {@link #CONFIG_PROPERTY_NAME_INTERVAL_SIZE}, leading to a bin size of 1
	 * second.
	 */
	public static final String CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_SECOND = "1";

	private final TimeUnit timeunit;
	private final long intervalSize;

	private long currentInterval = -1; // synchronized by (this)
	private long recordsInInterval; // synchronized by (this)

	public MonitoringThroughputFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid TimeUnit! Using inherited value of {} instead.", configTimeunitProperty, this.timeunit.name());
			configTimeunit = this.timeunit;
		}
		this.intervalSize = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE), configTimeunit);

	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE, Long.toString(this.intervalSize));
		return configuration;
	}

	@InputPort(name = INPUT_PORT_NAME_RECORDS, eventTypes = {
		IMonitoringRecord.class }, description = "Receives incoming records to calculate the throughput")
	public final void inputRecord(final IMonitoringRecord record) {
		// we assume a more or less linear order of incoming records
		final long timestamp = record.getLoggingTimestamp();
		final long interval = timestamp / this.intervalSize;
		synchronized (this) {
			if (interval < this.currentInterval) { // do not count records earlier than the current interval
				super.deliver(OUTPUT_PORT_NAME_UNCOUNTED_RECORDS, record);
			} else {
				if (interval > this.currentInterval) { // we enter a new interval
					if (this.currentInterval != -1) { // close all previous intervals if not the first interval
						super.deliver(OUTPUT_PORT_NAME_THROUGHPUT, this.recordsInInterval);
						for (long i = this.currentInterval + 1; i < interval; i++) {
							super.deliver(OUTPUT_PORT_NAME_THROUGHPUT, 0L);
						}
					}
					this.currentInterval = interval;
					this.recordsInInterval = 0;
				}
				this.recordsInInterval = this.recordsInInterval + 1;
			}
		}
		super.deliver(OUTPUT_PORT_NAME_RELAYED_RECORDS, record);
	}
}
