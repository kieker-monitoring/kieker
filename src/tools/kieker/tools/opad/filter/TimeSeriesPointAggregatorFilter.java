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

package kieker.tools.opad.filter;

import java.util.concurrent.ConcurrentHashMap;
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
import kieker.tools.util.AggregationVariableSet;

/**
 * This Filter aggregates the incoming DoubleTImeSeriesPoints over a configurable period of time.
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
@Plugin(name = "Variate TimeSeriesPoint Aggregator", outputPorts = {
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

	/** Saves the variables and the measurements, that are needed to calculate the intervals and the result for the aggregations per application. */
	private final ConcurrentHashMap<String, AggregationVariableSet> aggregationVariables;

	private final long aggregationSpan;
	private final TimeUnit timeunit;
	private final AggregationMethod aggregationMethod;

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

		this.aggregationVariables = new ConcurrentHashMap<String, AggregationVariableSet>();

		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = TimeUnit.MILLISECONDS;
		}
		this.timeunit = configTimeunit;
		this.aggregationSpan = TimeUnit.MILLISECONDS.convert(configuration.getIntProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN), this.timeunit);

		AggregationMethod configAggregationMethod;
		try {
			configAggregationMethod = AggregationMethod.valueOf(configuration
					.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD));
		} catch (final IllegalArgumentException ex) {
			configAggregationMethod = AggregationMethod.MEAN;
		}

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
		if (this.checkInitialization(input.getName())) {
			this.processInput(input, input.getTime(), input.getName());
		} else {
			// Initialization of the aggregation variables for a new application
			this.aggregationVariables.put(input.getName(), new AggregationVariableSet());
			this.processInput(input, input.getTime(), input.getName());
		}
	}

	/**
	 * Checks if the current application is already known to this filter.
	 * 
	 * @param name
	 *            Application name
	 */
	private boolean checkInitialization(final String name) {
		return this.aggregationVariables.containsKey(name);
	}

	private void processInput(final NamedDoubleTimeSeriesPoint input, final long currentTime, final String appname) {
		final AggregationVariableSet variables = this.aggregationVariables.get(appname);
		final long startOfTimestampsInterval = this.computeFirstTimestampInInterval(currentTime, variables);
		final long endOfTimestampsInterval = this.computeLastTimestampInInterval(currentTime, variables);
		// check if interval is omitted
		if (endOfTimestampsInterval > variables.getLastTimestampInCurrentInterval()) {
			if (variables.getFirstTimestampInCurrentInterval() >= 0) { // don't do this for the first record (only used for initialization of variables)
				this.calculateAggregationValue(variables);
				long numIntervalsElapsed = 1; // refined below
				numIntervalsElapsed = (endOfTimestampsInterval - variables.getLastTimestampInCurrentInterval()) / this.aggregationSpan;
				if (numIntervalsElapsed > 1) {
					for (int i = 1; i < numIntervalsElapsed; i++) {
						super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(variables.getLastTimestampInCurrentInterval()
								+ (i * this.aggregationSpan), 0.0,
								appname));
					}
				}

			}
			variables.setFirstTimestampInCurrentInterval(startOfTimestampsInterval);
			variables.setLastTimestampInCurrentInterval(endOfTimestampsInterval);
			variables.getAggregationList().clear();
		}
		variables.getAggregationList().add(input);

	}

	private void calculateAggregationValue(final AggregationVariableSet variables) {
		synchronized (this) {
			final int listSize = variables.getAggregationList().size();
			final double[] a = new double[listSize];
			for (int i = 0; i < listSize; i++) {
				a[i] = variables.getAggregationList().get(i).getValue();
			}
			final double aggregationValue = this.aggregationMethod.getAggregationValue(a);
			super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(variables.getLastTimestampInCurrentInterval(),
					aggregationValue,
					variables.getAggregationList().get(0).getName()));
			variables.getAggregationList().clear();
		}
	}

	/**
	 * Returns the first timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * 
	 * @return The timestamp in question.
	 */
	private long computeFirstTimestampInInterval(final long timestamp, final AggregationVariableSet variables) {
		final long referenceTimePoint;

		if (variables.getFirstIntervalStart() == -1) {
			variables.setFirstIntervalStart(timestamp);
		}

		referenceTimePoint = variables.getFirstIntervalStart();

		return referenceTimePoint + (((timestamp - referenceTimePoint) / this.aggregationSpan) * this.aggregationSpan);
	}

	/**
	 * Returns the last timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * @return The timestamp in question.
	 */
	private long computeLastTimestampInInterval(final long timestamp, final AggregationVariableSet variables) {
		final long referenceTimePoint = variables.getFirstIntervalStart();
		return referenceTimePoint + (((((timestamp - referenceTimePoint) / this.aggregationSpan) + 1) * this.aggregationSpan) - 1);
	}
}
