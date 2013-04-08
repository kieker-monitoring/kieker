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
import java.util.Timer;
import java.util.TimerTask;
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
	@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregator.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT) },
		configuration = {
			@Property(name = TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, defaultValue = "MEAN"),
			@Property(name = TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, defaultValue = "0"),
			@Property(name = TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, defaultValue = "MILLISECONDS")
		})
public class TimeSeriesPointAggregator extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	public static final String OUTPUT_PORT_NAME_AGGREGATED_TSPOINT = "aggregatedTSPoint";

	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_METHOD = "aggregationMethod";
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_SPAN = "aggregationSpan";
	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT = "timeUnit";

	private final List<NamedDoubleTimeSeriesPoint> aggregationList;
	private final long aggregationSpan;
	private TimeUnit timeunit = TimeUnit.MILLISECONDS;
	private volatile long aggregationStartTime;
	private AggregationMethod aggregationMethod;
	private volatile boolean firstEvent = true;

	public TimeSeriesPointAggregator(final Configuration configuration, final IProjectContext projectContext) {
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

	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregator.INPUT_PORT_NAME_TSPOINT)
	public void inputTSPoint(final NamedDoubleTimeSeriesPoint input) {
		// First TSPoint Event, so set the Task
		if (this.firstEvent) {
			this.firstEvent = false;
			final Timer aggragatorTimer = new Timer();
			final long spanInMillis = TimeUnit.MILLISECONDS.convert(this.aggregationSpan, this.timeunit);
			this.aggregationStartTime = input.getTime().getTime();
			aggragatorTimer.schedule(this.createTask(), spanInMillis, spanInMillis);
		}
		// Next TSPoint in the Span, so collect it
		this.aggregationList.add(input);
	}

	private TimerTask createTask() {
		final TimerTask t = new TimerTask() {
			@Override
			public void run() {
				TimeSeriesPointAggregator.this.calculateAggregationValue();
			}
		};
		return t;
	}

	private synchronized void calculateAggregationValue() {
		final int listSize = this.aggregationList.size();
		this.aggregationStartTime = this.aggregationStartTime + TimeUnit.MILLISECONDS.convert(this.aggregationSpan, this.timeunit);
		if (listSize == 0) {
			super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(new Date(this.aggregationStartTime), 0.0,
					"empty"));
		} else {
			final double[] a = new double[listSize];
			for (int i = 0; i < listSize; i++) {
				a[i] = this.aggregationList.get(i).getValue();
			}
			final double aggregationValue = this.aggregationMethod.getAggregationValue(a);
			super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, new NamedDoubleTimeSeriesPoint(new Date(this.aggregationStartTime),
					aggregationValue,
					this.aggregationList.get(0).getName()));
			this.aggregationList.clear();
		}
	}
}
