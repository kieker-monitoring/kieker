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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	private List<NamedDoubleTimeSeriesPoint> createInputEventSetForecast() {
		final List<NamedDoubleTimeSeriesPoint> retList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.3));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.4));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.5));
		return retList;
	}

	// @Before
	// public void setUp() throws IllegalStateException,
	// AnalysisConfigurationException {
	// this.controller = new AnalysisController();
	//
	// // READER
	// final Configuration readerAggregationConfiguration = new Configuration();
	// readerAggregationConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
	// this.theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration);
	// this.theReaderAggregator.addAllObjects(this.createInputEventSetForecast());
	// this.controller.registerReader(this.theReaderAggregator);
	//
	// // AGGREGATIONFILTER
	// final Configuration aggregationConfiguration = new Configuration();
	// aggregationConfiguration.setProperty(TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "0");
	// this.aggregator = new TimeSeriesPointAggregator(aggregationConfiguration);
	// this.controller.registerFilter(this.aggregator);
	//
	// // SINK 1
	// this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
	// Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
	// this.controller.registerFilter(this.sinkPlugin);
	//
	// // CONNECTION
	// this.controller.connect(this.theReaderAggregator, ListReader.OUTPUT_PORT_NAME, this.aggregator, TimeSeriesPointAggregator.INPUT_PORT_NAME_TSPOINT);
	// this.controller.connect(this.aggregator,
	// TimeSeriesPointAggregator.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, this.sinkPlugin,
	// ListCollectionFilter.INPUT_PORT_NAME);
	// }

	// Test for the Aggregator Filter
	@Test
	public void testAggregatorOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		this.controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		// readerAggregationConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration);
		this.controller.registerReader(this.theReaderAggregator);
		this.theReaderAggregator.addObject(this.createNDTSP(OP_SIGNATURE_A, 0.1));
		int i = 1;
		while (i < 10000) {
			this.theReaderAggregator.addObject(this.createNDTSP(OP_SIGNATURE_A, i));
			i++;
		}

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregator.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "100");
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

}
