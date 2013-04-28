/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.AggregationMethod;

/**
 * This Filter aggregates the incoming DoubleTImeSeriesPoints over a configurable period of time.
 * 
 * @author Tom Frotscher
 * 
 */
@Plugin(name = "TimeSeriesPoint Aggregator", outputPorts = {
	@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT) },
		configuration = {
			@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, defaultValue = "MEAN"),
			@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, defaultValue = "1000"),
			@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, defaultValue = "MILLISECONDS")
		})
public class TimeSeriesPointAggregatorFilter extends AbstractFilterPlugin {

	/**
	 * The name of the input port receiving the measurements.
	 */
	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	/**
	 * The name of the output port delivering the aggregated time series point.
	 */
	public static final String OUTPUT_PORT_NAME_AGGREGATED_TSPOINT = "aggregatedTSPoint";

	/** The name of the property determining the aggregation method. */
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_METHOD = "aggregationMethod";
	/** The name of the property determining the aggregation time span. */
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_SPAN = "aggregationSpan";
	/** The name of the property determining the time unit of the aggregation time span. */
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT = "timeUnit";

	/** Saves the measurements of the current time span, until the span is closed. */
	private final List<NamedDoubleTimeSeriesPoint> aggregationList;

	private final long aggregationSpan;
	private TimeUnit timeunit = TimeUnit.MILLISECONDS;
	private AggregationMethod aggregationMethod = AggregationMethod.MEAN;
	private volatile long firstIntervalStart = -1;
	private volatile long firstTimestampInCurrentInterval = -1; // initialized with the first incoming event
	private volatile long lastTimestampInCurrentInterval = -1; // initialized with the first incoming event

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param configuration
	 *            The configuration for this component
	 * @param projectContext
	 *            The projectContext for this component
	 */
	public TimeSeriesPointAggregatorFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.aggregationList = Collections.synchronizedList(new ArrayList<NamedDoubleTimeSeriesPoint>());
		this.aggregationSpan = configuration.getIntProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN);
		TimeUnit configTimeunit;
		AggregationMethod configAggregationMethod;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = this.timeunit;
		}
		this.timeunit = configTimeunit;
		try {
			configAggregationMethod = AggregationMethod.valueOf(configuration
					.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD));
		} catch (final IllegalArgumentException ex) {
			configAggregationMethod = this.aggregationMethod;
		}
		this.aggregationMethod = AggregationMethod.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD));
		this.aggregationMethod = configAggregationMethod;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, Long.toString(this.aggregationSpan));
		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, this.aggregationMethod.name());
		return configuration;
	}

	/**
	 * This method represents the input port for the incoming measurements.
	 * 
	 * @param input
	 *            The next incoming measurement
	 */
	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputTSPoint(final NamedDoubleTimeSeriesPoint input) {
		this.processInput(input, input.getTime().getTime());
	}

	private void processInput(final NamedDoubleTimeSeriesPoint input, final long currentTime) {
		final long startOfTimestampsInterval = this.computeFirstTimestampInInterval(currentTime);
		final long endOfTimestampsInterval = this.computeLastTimestampInInterval(currentTime);
		// check if interval is omitted
		if (endOfTimestampsInterval > this.lastTimestampInCurrentInterval) {
			if (this.firstTimestampInCurrentInterval >= 0) { // don't do this for the first record (only used for initialization of variables)
				this.calculateAggregationValue();
				long numIntervalsElapsed = 1; // refined below
				numIntervalsElapsed = (endOfTimestampsInterval - this.lastTimestampInCurrentInterval) / this.aggregationSpan;
				if (numIntervalsElapsed > 1) {
					for (int i = 1; i < numIntervalsElapsed; i++) {
						// TODO: add zero-Value for exceeded intervals
						System.out.println("exceeded");
					}
				}

			}
			this.firstTimestampInCurrentInterval = startOfTimestampsInterval;
			this.lastTimestampInCurrentInterval = endOfTimestampsInterval;
			this.aggregationList.clear();
		}
		this.aggregationList.add(input);

	}

	private synchronized void calculateAggregationValue() {
		final int listSize = this.aggregationList.size();
		// this.aggregationStartTime = this.aggregationStartTime + TimeUnit.MILLISECONDS.convert(this.aggregationSpan, this.timeunit);
		// if (listSize == 0) {
		// super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(new Date(this.aggregationStartTime), 0.0,
		// "empty"));
		// } else {
		// debugging output: System.out.println(this.aggregationList.toString());
		final double[] a = new double[listSize];
		for (int i = 0; i < listSize; i++) {
			a[i] = this.aggregationList.get(i).getValue();
		}
		final double aggregationValue = this.aggregationMethod.getAggregationValue(a);
		super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(new Date(this.lastTimestampInCurrentInterval),
				aggregationValue,
				this.aggregationList.get(0).getName()));
		this.aggregationList.clear();
		// }
	}

	/**
	 * Returns the first timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * 
	 * @return The timestamp in question.
	 */
	private long computeFirstTimestampInInterval(final long timestamp) {
		final long referenceTimePoint;

		if (this.firstIntervalStart == -1) {
			this.firstIntervalStart = timestamp;
		}

		referenceTimePoint = this.firstIntervalStart;

		return referenceTimePoint + (((timestamp - referenceTimePoint) / this.aggregationSpan) * this.aggregationSpan);
	}

	/**
	 * Returns the last timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * @return The timestamp in question.
	 */
	private long computeLastTimestampInInterval(final long timestamp) {
		final long referenceTimePoint = this.firstIntervalStart;
		return referenceTimePoint + (((((timestamp - referenceTimePoint) / this.aggregationSpan) + 1) * this.aggregationSpan) - 1);
	}
}
