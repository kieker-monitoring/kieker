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
import java.util.Date;
import java.util.List;

import org.apache.commons.math.stat.StatUtils;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * This Filter aggregates the incoming responsetimes over a configurable period of time.
 * 
 * @author Tom Frotscher
 * 
 */
@Plugin(name = "TimeSeriesPoint Aggregator", outputPorts = {
	@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregator.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT) },
		configuration = {
			@Property(name = TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, defaultValue = "0")
		// @Property(name = TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, defaultValue = "NANOSECONDS")
		})
public class TimeSeriesPointAggregator extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	public static final String OUTPUT_PORT_NAME_AGGREGATED_TSPOINT = "aggregatedTSPoint";

	public static final String CONFIG_PROPERTY_NAME_AGGREGATION_SPAN = "aggregationSpan";
	// public static final String CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT = "timeUnit";

	private final List<NamedDoubleTimeSeriesPoint> aggregationList;
	private final int aggregationSpan;
	// private final String timeUnit;
	private long aggregationStartTime;

	public TimeSeriesPointAggregator(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.aggregationList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		this.aggregationSpan = configuration.getIntProperty(CONFIG_PROPERTY_NAME_AGGREGATION_SPAN);
		// this.timeUnit = configuration.getStringProperty(CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT);
	}

	@Deprecated
	public TimeSeriesPointAggregator(final Configuration configuration) {
		this(configuration, null);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = TimeSeriesPointAggregator.INPUT_PORT_NAME_TSPOINT)
	public void inputTSPoint(final NamedDoubleTimeSeriesPoint input) {
		// First TSPoint Event, so set the Starttimer
		if (this.aggregationList.size() == 0) {
			this.aggregationStartTime = input.getTime().getTime();
		}
		// Next TSPoint in the Span, so collect it
		if ((input.getTime().getTime() - this.aggregationStartTime) <= this.aggregationSpan) {
			this.aggregationList.add(input);
			// Span exceeded, calculate Mean
		} else {
			final NamedDoubleTimeSeriesPoint result = this.calculateMean(input.getName());
			this.aggregationList.clear();
			this.aggregationList.add(input);
			// System.out.println("Mean: " + result.getValue() + " - " + result.getTime().getTime());
			this.aggregationStartTime = this.aggregationStartTime + this.aggregationSpan;
			super.deliver(OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, result);
		}
	}

	private NamedDoubleTimeSeriesPoint calculateMean(final String n) {
		final int listSize = this.aggregationList.size();
		final double[] a = new double[listSize];
		for (int i = 0; i < listSize; i++) {
			a[i] = this.aggregationList.get(i).getValue();
		}
		final double meanTSValue = StatUtils.mean(a);
		return new NamedDoubleTimeSeriesPoint(new Date(this.aggregationStartTime + this.aggregationSpan), meanTSValue, n);
	}
}
