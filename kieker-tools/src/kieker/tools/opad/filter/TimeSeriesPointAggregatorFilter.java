/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.opad.record.AggregationWindow;
import kieker.tools.opad.timeseries.AggregationMethod;
import kieker.tools.util.AggregationVariableSet;

/**
 * This Filter aggregates the incoming DoubleTImeSeriesPoints over a configurable period of time.
 *
 * @author Tom Frotscher, Teerat Pitakrat
 * @since 1.10
 */
@Plugin(name = "Variate TimeSeriesPoint Aggregator", outputPorts = {
		@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT),
		@OutputPort(eventTypes = { AggregationWindow.class }, name = TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATION_WINDOW) },
		configuration = {
		@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, defaultValue = "MEAN"),
		@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, defaultValue = "1000"),
		@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, defaultValue = "MILLISECONDS"),
		@Property(name = TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE, defaultValue = "perVariable")
})
public class TimeSeriesPointAggregatorFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";
	public static final String OUTPUT_PORT_NAME_AGGREGATED_TSPOINT = "aggregatedTSPoint";

	/**
	 * The name of the output port delivering the aggregated window.
	 */
	public static final String OUTPUT_PORT_NAME_AGGREGATION_WINDOW = "aggregationWindow";

	/** The name of the property determining the aggregation method. */
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_METHOD = "aggregationMethod";
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_SPAN = "aggregationSpan";
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT = "timeUnit";
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE = "timeScope";

	/**
	 * Time scope
	 *
	 * PER_VARIABLE: The aggregated value of each variable is processed only when a new input of that variable arrives. This is the default behavior.
	 * GLOBAL: All variables are processed when any input arrives.
	 */
	public static final String CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_PER_VARIABLE = "perVariable";
	public static final String CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_GLOBAL = "global";

	/** Saves the variables and the measurements, that are needed to calculate the intervals and the result for the aggregations per application. */
	private final ConcurrentHashMap<String, AggregationVariableSet> aggregationVariables;

	private final long aggregationSpan; // default from annotation used
	private final TimeUnit timeunit; // default from annotation used
	private final AggregationMethod aggregationMethod; // default from annotation used
	private final boolean aggregationTimescopeGlobal;

	private AggregationWindow recentWindow = new AggregationWindow(0L, 0L);

	public TimeSeriesPointAggregatorFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.aggregationVariables = new ConcurrentHashMap<String, AggregationVariableSet>();

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		// Determine Aggregation time unit
		TimeUnit configTimeUnit;
		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT);
		try {
			configTimeUnit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			configTimeUnit = this.timeunit;
			this.log.warn(configTimeunitProperty + " is no valid TimeUnit! Using inherited value of " + configTimeUnit.name() + " instead.");
		}

		// Determine aggregation span method
		AggregationMethod configAggregationMethod;
		try {
			configAggregationMethod = AggregationMethod.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD));
		} catch (final IllegalArgumentException ex) {
			configAggregationMethod = AggregationMethod.valueOf("MEAN");
		}
		this.aggregationMethod = configAggregationMethod;

		// Determine aggregation span
		this.aggregationSpan = this.timeunit.convert(configuration.getIntProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN), configTimeUnit);

		// Determine realtime processing flag
		final String scope = configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE);
		if (CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_GLOBAL.equals(scope)) {
			this.aggregationTimescopeGlobal = true;
		} else {
			this.aggregationTimescopeGlobal = false;
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, Long.toString(this.aggregationSpan));
		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, this.aggregationMethod.name());
		if (this.aggregationTimescopeGlobal) {
			configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE, CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_GLOBAL);
		} else {
			configuration.setProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE, CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_PER_VARIABLE);
		}

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
		final String name = input.getName();
		this.aggregationVariables.putIfAbsent(name, new AggregationVariableSet());

		if (this.aggregationTimescopeGlobal) {
			this.processInputGlobalScope(input);
		} else {
			this.processInputVariableScope(input);
		}
	}

	private synchronized void processInputVariableScope(final NamedDoubleTimeSeriesPoint input) {
		final long currentTime = input.getTime();
		final String appname = input.getName();
		final AggregationVariableSet variables = this.aggregationVariables.get(appname);
		final long startOfTimestampsInterval = this.computeFirstTimestampInInterval(currentTime, variables);
		final long endOfTimestampsInterval = this.computeLastTimestampInInterval(currentTime, variables);

		if (this.recentWindow.getWindowEnd() != endOfTimestampsInterval) {
			this.recentWindow = new AggregationWindow(startOfTimestampsInterval, endOfTimestampsInterval);
			super.deliver(OUTPUT_PORT_NAME_AGGREGATION_WINDOW, this.recentWindow);
		}

		// check if interval is omitted
		if (endOfTimestampsInterval > variables.getLastTimestampInCurrentInterval()) {
			if (variables.getFirstTimestampInCurrentInterval() >= 0) { // don't do this for the first record (only used for initialization of variables)
				this.calculateAndDeliverAggregationValue(variables);
				long numIntervalsElapsed = 1; // refined below
				numIntervalsElapsed = (endOfTimestampsInterval - variables.getLastTimestampInCurrentInterval()) / this.aggregationSpan;
				if (numIntervalsElapsed > 1) {
					for (int i = 1; i < numIntervalsElapsed; i++) {
						super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(variables.getLastTimestampInCurrentInterval()
								+ (i * this.aggregationSpan), Double.NaN, // Note: Count filter should use 0.0
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

	private synchronized void processInputGlobalScope(final NamedDoubleTimeSeriesPoint input) {
		final long inputTimestamp = input.getTime();
		final AggregationVariableSet inputVariables = this.aggregationVariables.get(input.getName());
		final long startOfInputTimestampsInterval = this.computeFirstTimestampInInterval(inputTimestamp, inputVariables);
		final long endOfInputTimestampsInterval = this.computeLastTimestampInInterval(inputTimestamp, inputVariables);

		if (this.recentWindow.getWindowEnd() != endOfInputTimestampsInterval) {
			this.recentWindow = new AggregationWindow(startOfInputTimestampsInterval, endOfInputTimestampsInterval);
			super.deliver(OUTPUT_PORT_NAME_AGGREGATION_WINDOW, this.recentWindow);
		}

		final Map<Long, List<NamedDoubleTimeSeriesPoint>> orderedTsPoints = new TreeMap<Long, List<NamedDoubleTimeSeriesPoint>>();

		for (final String appname : this.aggregationVariables.keySet()) {
			final AggregationVariableSet variables = this.aggregationVariables.get(appname);
			final long startOfLatestIntervalTimestamp = this.computeFirstTimestampInInterval(inputTimestamp, variables);
			final long endOfLatestIntervalTimestamp = this.computeLastTimestampInInterval(inputTimestamp, variables);
			long lastTimestampInCurrentInterval = variables.getLastTimestampInCurrentInterval();
			if (endOfLatestIntervalTimestamp > lastTimestampInCurrentInterval) {
				if (variables.getFirstTimestampInCurrentInterval() >= 0) {
					// Skip if this is the first record
					lastTimestampInCurrentInterval = variables.getLastTimestampInCurrentInterval();
					while (lastTimestampInCurrentInterval < inputTimestamp) {
						final NamedDoubleTimeSeriesPoint newTsPoint = this.calculateAggregationValueOfCurrentInterval(variables, appname);
						this.addNewTsPoint(orderedTsPoints, newTsPoint);
						lastTimestampInCurrentInterval = variables.getLastTimestampInCurrentInterval();
					}
				} else {
					// Set interval timestamp if this is the first record
					variables.setFirstTimestampInCurrentInterval(startOfLatestIntervalTimestamp);
					variables.setLastTimestampInCurrentInterval(endOfLatestIntervalTimestamp);
				}
			}
		}

		// Ignore input if the timestamp is before the current window
		if (inputTimestamp >= inputVariables.getFirstTimestampInCurrentInterval()) {
			inputVariables.getAggregationList().add(input);
		}

		for (final long timestamp : orderedTsPoints.keySet()) {
			final List<NamedDoubleTimeSeriesPoint> tsPointList = orderedTsPoints.get(timestamp);
			for (final NamedDoubleTimeSeriesPoint tsPoint : tsPointList) {
				super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, tsPoint);
			}
		}
	}

	private NamedDoubleTimeSeriesPoint calculateAggregationValueOfCurrentInterval(final AggregationVariableSet variables, final String name) {
		final double aggregationValue;
		final NamedDoubleTimeSeriesPoint tsPoint;
		final long firstTimestampInCurrentInterval = variables.getFirstTimestampInCurrentInterval();
		final long lastTimestampInCurrentInterval = variables.getLastTimestampInCurrentInterval();
		synchronized (this) {
			final int listSize = variables.getAggregationList().size();
			if (listSize <= 0) {
				tsPoint = new NamedDoubleTimeSeriesPoint(lastTimestampInCurrentInterval, Double.NaN, name);
			} else {
				final double[] a = new double[listSize];
				for (int i = 0; i < listSize; i++) {
					a[i] = variables.getAggregationList().get(i).getValue();
				}
				aggregationValue = this.aggregationMethod.getAggregationValue(a);
				tsPoint = new NamedDoubleTimeSeriesPoint(lastTimestampInCurrentInterval, aggregationValue, name);
				variables.getAggregationList().clear();
			}
			variables.setFirstTimestampInCurrentInterval(firstTimestampInCurrentInterval + this.aggregationSpan);
			variables.setLastTimestampInCurrentInterval(lastTimestampInCurrentInterval + this.aggregationSpan);
		}
		return tsPoint;
	}

	private void addNewTsPoint(final Map<Long, List<NamedDoubleTimeSeriesPoint>> orderedTsPoints, final NamedDoubleTimeSeriesPoint newTsPoint) {
		final long newTsPointTimestamp = newTsPoint.getTime();
		if (!orderedTsPoints.containsKey(newTsPointTimestamp)) {
			orderedTsPoints.put(newTsPointTimestamp, new ArrayList<NamedDoubleTimeSeriesPoint>());
		}
		final List<NamedDoubleTimeSeriesPoint> tsPointList = orderedTsPoints.get(newTsPointTimestamp);
		tsPointList.add(newTsPoint);
	}

	private void calculateAndDeliverAggregationValue(final AggregationVariableSet variables) {
		final double aggregationValue;
		final NamedDoubleTimeSeriesPoint tsPoint;
		synchronized (this) {
			final int listSize = variables.getAggregationList().size();
			final double[] a = new double[listSize];
			for (int i = 0; i < listSize; i++) {
				a[i] = variables.getAggregationList().get(i).getValue();
			}
			aggregationValue = this.aggregationMethod.getAggregationValue(a);
			tsPoint = new NamedDoubleTimeSeriesPoint(variables.getLastTimestampInCurrentInterval(),
					aggregationValue,
					variables.getAggregationList().get(0).getName()); // use name of first element (any will do)
			variables.getAggregationList().clear();

		}
		super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, tsPoint);
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
