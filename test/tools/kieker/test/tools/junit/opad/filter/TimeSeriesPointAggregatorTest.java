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

package kieker.test.tools.junit.opad.filter;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.TimeSeriesPointAggregatorFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Checks if values in the given timespan (10 milliseconds) are aggregated correctly.
 * Also checks if zero values are created for timestamps with no incoming values.
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
public class TimeSeriesPointAggregatorTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";
	private static final String OP_SIGNATURE_C = "c.C.opC";

	private AnalysisController controller;

	// Variables ForecastingFilter
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator;
	private TimeSeriesPointAggregatorFilter aggregator;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin;

	/**
	 * Creates an instance of this class.
	 */
	public TimeSeriesPointAggregatorTest() {
		// empty default constructor
	}

	// HelperMethods ForecastingFilter
	private NamedDoubleTimeSeriesPoint createNDTSP(final long timestamp, final double value, final String signature) {
		return new NamedDoubleTimeSeriesPoint(timestamp, value, signature);
	}

	/**
	 * Test of the VariateTSPointAggregator, including the tests setup.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Test
	public void testAggregatorOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		this.controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		this.theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration, this.controller);

		this.theReaderAggregator.addObject(this.createNDTSP(1369127812664L, 1000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812665L, 2000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812664L, 1000, OP_SIGNATURE_C));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812668L, 5500, OP_SIGNATURE_C));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 2000, OP_SIGNATURE_B));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 3000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812684L, 4000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 2000, OP_SIGNATURE_C));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812684L, 5000, OP_SIGNATURE_B));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812687L, 1000, OP_SIGNATURE_B));
		// Skipped two timespans for application A, so two empty points should be created
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812714L, 6000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812724L, 7000, OP_SIGNATURE_A));
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812694L, 5000, OP_SIGNATURE_B));
		// Skipped one timespans for application B, so one empty point should be created
		this.theReaderAggregator.addObject(this.createNDTSP(1369127812714L, 5000, OP_SIGNATURE_B));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "10");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "MILLISECONDS");
		this.aggregator = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, this.controller);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(), this.controller);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

		// CONNECTION
		this.controller.connect(this.theReaderAggregator, ListReader.OUTPUT_PORT_NAME, this.aggregator, TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.aggregator,
				TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, this.sinkPlugin.getList().size());
		this.controller.run();

		// Expected: (1000 + 2000) / 2 = 1500 Application A
		Assert.assertEquals(new Double(1500), Double.valueOf(this.sinkPlugin.getList().get(0).getDoubleValue()));
		// Expected: 3000 Application A
		Assert.assertEquals(new Double(3000), Double.valueOf(this.sinkPlugin.getList().get(1).getDoubleValue()));
		// Expected: (1000 + 5500) / 2 = 3250 Application C
		Assert.assertEquals(new Double(3250), Double.valueOf(this.sinkPlugin.getList().get(2).getDoubleValue()));
		// Expected: 2000 Application B
		Assert.assertEquals(new Double(2000), Double.valueOf(this.sinkPlugin.getList().get(3).getDoubleValue()));
		// Expected: 4000 Application A
		Assert.assertEquals(new Double(4000), Double.valueOf(this.sinkPlugin.getList().get(4).getDoubleValue()));
		// Expected: Skipped two spans for Application A -> 2 time 0
		Assert.assertEquals(new Double(0), Double.valueOf(this.sinkPlugin.getList().get(5).getDoubleValue()));
		Assert.assertEquals(new Double(0), Double.valueOf(this.sinkPlugin.getList().get(6).getDoubleValue()));
		// Expected: 6000 Application A
		Assert.assertEquals(new Double(6000), Double.valueOf(this.sinkPlugin.getList().get(7).getDoubleValue()));
		// Expected: (5000 + 1000) / 2 = 3000 Application B
		Assert.assertEquals(new Double(3000), Double.valueOf(this.sinkPlugin.getList().get(8).getDoubleValue()));
		// Expected: 5000 Application B
		Assert.assertEquals(new Double(5000), Double.valueOf(this.sinkPlugin.getList().get(9).getDoubleValue()));
		// Expected: Skipped one span for Application B -> 1 time 0
		Assert.assertEquals(new Double(0), Double.valueOf(this.sinkPlugin.getList().get(10).getDoubleValue()));

	}
}
