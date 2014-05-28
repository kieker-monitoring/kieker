/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.tools.junit.opad.filter;

import org.junit.Assert;
import org.junit.Test;

import kicker.analysis.AnalysisController;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.forward.ListCollectionFilter;
import kicker.analysis.plugin.reader.list.ListReader;
import kicker.common.configuration.Configuration;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.tools.opad.filter.TimeSeriesPointAggregatorFilter;
import kicker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * Checks if values in the given timespan (10 milliseconds) are aggregated correctly.
 * Also checks if zero values are created for timestamps with no incoming values.
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
public class TimeSeriesPointAggregatorTest extends AbstractKickerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";
	private static final String OP_SIGNATURE_C = "c.C.opC";

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

		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		theReaderAggregator.addObject(this.createNDTSP(1369127812664L, 1000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812665L, 2000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812664L, 1000, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(1369127812668L, 5500, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 2000, OP_SIGNATURE_B));
		theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 3000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812684L, 4000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812674L, 2000, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(1369127812684L, 5000, OP_SIGNATURE_B));
		theReaderAggregator.addObject(this.createNDTSP(1369127812687L, 1000, OP_SIGNATURE_B));
		// Skipped two timespans for application A, so two empty points should be created
		theReaderAggregator.addObject(this.createNDTSP(1369127812714L, 6000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812724L, 7000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(1369127812694L, 5000, OP_SIGNATURE_B));
		// Skipped one timespans for application B, so one empty point should be created
		theReaderAggregator.addObject(this.createNDTSP(1369127812714L, 5000, OP_SIGNATURE_B));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "10");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "MILLISECONDS");
		final TimeSeriesPointAggregatorFilter aggregator = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, controller);

		// SINK 1
		final ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(),
				controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		// CONNECTION
		controller.connect(theReaderAggregator, ListReader.OUTPUT_PORT_NAME, aggregator, TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		controller.connect(aggregator, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, sinkPlugin.getList().size());
		controller.run();

		// Expected: (1000 + 2000) / 2 = 1500 Application A
		Assert.assertEquals(Double.valueOf(1500), Double.valueOf(sinkPlugin.getList().get(0).getDoubleValue()));
		// Expected: 3000 Application A
		Assert.assertEquals(Double.valueOf(3000), Double.valueOf(sinkPlugin.getList().get(1).getDoubleValue()));
		// Expected: (1000 + 5500) / 2 = 3250 Application C
		Assert.assertEquals(Double.valueOf(3250), Double.valueOf(sinkPlugin.getList().get(2).getDoubleValue()));
		// Expected: 2000 Application B
		Assert.assertEquals(Double.valueOf(2000), Double.valueOf(sinkPlugin.getList().get(3).getDoubleValue()));
		// Expected: 4000 Application A
		Assert.assertEquals(Double.valueOf(4000), Double.valueOf(sinkPlugin.getList().get(4).getDoubleValue()));
		// Expected: Skipped two spans for Application A -> 2 time 0
		Assert.assertEquals(Double.valueOf(0), Double.valueOf(sinkPlugin.getList().get(5).getDoubleValue()));
		Assert.assertEquals(Double.valueOf(0), Double.valueOf(sinkPlugin.getList().get(6).getDoubleValue()));
		// Expected: 6000 Application A
		Assert.assertEquals(Double.valueOf(6000), Double.valueOf(sinkPlugin.getList().get(7).getDoubleValue()));
		// Expected: (5000 + 1000) / 2 = 3000 Application B
		Assert.assertEquals(Double.valueOf(3000), Double.valueOf(sinkPlugin.getList().get(8).getDoubleValue()));
		// Expected: 5000 Application B
		Assert.assertEquals(Double.valueOf(5000), Double.valueOf(sinkPlugin.getList().get(9).getDoubleValue()));
		// Expected: Skipped one span for Application B -> 1 time 0
		Assert.assertEquals(Double.valueOf(0), Double.valueOf(sinkPlugin.getList().get(10).getDoubleValue()));

	}
}
