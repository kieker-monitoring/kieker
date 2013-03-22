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

package kieker.test.tools.junit.opad.filter;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.TimeSeriesPointAggregator;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * 
 * @author Tom Frotscher
 * 
 */
public class TimeSeriesPointAggregatorTest {

	private AnalysisController controller;

	// Variables ForecastingFilter
	private static final String OP_SIGNATURE_A = "a.A.opA";
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator;
	private TimeSeriesPointAggregator aggregator;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin;

	// HelperMethods ForecastingFilter

	private NamedDoubleTimeSeriesPoint createNDTSP(final String signature, final double value) {
		final NamedDoubleTimeSeriesPoint r = new NamedDoubleTimeSeriesPoint(new Date(), value, signature);
		return r;
	}

	private NamedDoubleTimeSeriesPoint createNDTSP(final String signature, final Date date, final double value) {
		final NamedDoubleTimeSeriesPoint r = new NamedDoubleTimeSeriesPoint(date, value, signature);
		return r;
	}

	// Test for the Aggregator Filter
	// @Test
	public void testAggregatorOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		this.controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		// readerAggregationConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration);
		this.controller.registerReader(this.theReaderAggregator);
		int i = 1;
		while (i < 100) {
			this.theReaderAggregator.addObject(this.createNDTSP(OP_SIGNATURE_A, i));
			i++;
		}

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "5");
		this.aggregator = new TimeSeriesPointAggregator(aggregationConfiguration);
		this.controller.registerFilter(this.aggregator);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.controller.registerFilter(this.sinkPlugin);

		// CONNECTION
		this.controller.connect(this.theReaderAggregator, ListReader.OUTPUT_PORT_NAME, this.aggregator, TimeSeriesPointAggregator.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.aggregator,
				TimeSeriesPointAggregator.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, this.sinkPlugin.getList().size());
		this.controller.run();

	}

	/*
	 * Fill the Filter with Events with Timestamps from 0 to 12 (stepsize: 1) and values from 1 to 12 (stepsize: 1)
	 * With an aggregationspan of 5ms, the values for the timestamps from 0 to 5 (-> 1,2,3,4,5,6 - Mean = 3.5) and
	 * 6 to 11 (-> 7,8,9,10,11 - Mean = 9.0) should be aggregated.
	 */
	@Test
	public void testAggregatorWithKnownTimestamps() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		this.controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		// readerAggregationConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration);
		this.controller.registerReader(this.theReaderAggregator);

		// start date in ms
		long d = 0L;
		int i = 1;
		// fill the filter
		while (i <= 12) {
			this.theReaderAggregator.addObject(this.createNDTSP(OP_SIGNATURE_A, new Date(d), i));
			d = d + 1;
			i++;
		}

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "5");
		this.aggregator = new TimeSeriesPointAggregator(aggregationConfiguration);
		this.controller.registerFilter(this.aggregator);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.controller.registerFilter(this.sinkPlugin);

		// CONNECTION
		this.controller.connect(this.theReaderAggregator, ListReader.OUTPUT_PORT_NAME, this.aggregator, TimeSeriesPointAggregator.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.aggregator,
				TimeSeriesPointAggregator.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, this.sinkPlugin.getList().size());
		this.controller.run();

		Assert.assertEquals(2, this.sinkPlugin.getList().size());
		// Mean of first timespan should be 3.5
		Assert.assertEquals(3.5, this.sinkPlugin.getList().get(0).getDoubleValue(), 0);
		// Mean of second timespan should be 9.0
		Assert.assertEquals(9.0, this.sinkPlugin.getList().get(1).getDoubleValue(), 0);

	}
}
