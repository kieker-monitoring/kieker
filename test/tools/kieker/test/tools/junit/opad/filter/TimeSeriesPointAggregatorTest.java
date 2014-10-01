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
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Checks if values in the given timespan (10 milliseconds) are aggregated correctly.
 * Also checks if zero values are created for timestamps with no incoming values.
 *
 * @author Tom Frotscher
 * @since 1.10
 *
 */
public class TimeSeriesPointAggregatorTest extends AbstractKiekerTest {

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

	@Test
	public void testAggregatorOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		theReaderAggregator.addObject(this.createNDTSP(664L, 1000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(665L, 2000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(664L, 1000, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(668L, 5500, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(674L, 2000, OP_SIGNATURE_B));
		theReaderAggregator.addObject(this.createNDTSP(674L, 3000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(684L, 4000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(674L, 2000, OP_SIGNATURE_C));
		theReaderAggregator.addObject(this.createNDTSP(684L, 5000, OP_SIGNATURE_B));
		theReaderAggregator.addObject(this.createNDTSP(687L, 1000, OP_SIGNATURE_B));
		// Skipped two timespans for application A, so two empty points should be created
		theReaderAggregator.addObject(this.createNDTSP(714L, 6000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(724L, 7000, OP_SIGNATURE_A));
		theReaderAggregator.addObject(this.createNDTSP(694L, 5000, OP_SIGNATURE_B));
		// Skipped one timespans for application B, so one empty point should be created
		theReaderAggregator.addObject(this.createNDTSP(714L, 5000, OP_SIGNATURE_B));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "10");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "NANOSECONDS");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
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

		Assert.assertEquals(11, sinkPlugin.getList().size());
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
		Assert.assertEquals(Double.NaN, sinkPlugin.getList().get(5).getDoubleValue(), 0.0000001d);
		Assert.assertEquals(Double.NaN, sinkPlugin.getList().get(6).getDoubleValue(), 0.0000001d);
		// Expected: 6000 Application A
		Assert.assertEquals(Double.valueOf(6000), Double.valueOf(sinkPlugin.getList().get(7).getDoubleValue()));
		// Expected: (5000 + 1000) / 2 = 3000 Application B
		Assert.assertEquals(Double.valueOf(3000), Double.valueOf(sinkPlugin.getList().get(8).getDoubleValue()));
		// Expected: 5000 Application B
		Assert.assertEquals(Double.valueOf(5000), Double.valueOf(sinkPlugin.getList().get(9).getDoubleValue()));
		// Expected: Skipped one span for Application B -> 1 time 0
		Assert.assertEquals(Double.NaN, sinkPlugin.getList().get(10).getDoubleValue(), 0.0000001d);
	}

	@Test
	public void testTimestampTimeUnitNano() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> listReader = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		listReader.addObject(this.createNDTSP(1L, 3, OP_SIGNATURE_A));
		listReader.addObject(this.createNDTSP(13L, 13, OP_SIGNATURE_A));
		listReader.addObject(this.createNDTSP(16L, 16, OP_SIGNATURE_A));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "5");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "NANOSECONDS");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
		final TimeSeriesPointAggregatorFilter aggregator = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, controller);

		// SINK
		final ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(),
				controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		// CONNECTION
		controller.connect(listReader, ListReader.OUTPUT_PORT_NAME, aggregator, TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		controller.connect(aggregator, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, sinkPlugin.getList().size());
		controller.run();

		Assert.assertEquals(3, sinkPlugin.getList().size());

		// check for correct windows
		Assert.assertEquals(5L, sinkPlugin.getList().get(0).getTime());
		Assert.assertEquals(10L, sinkPlugin.getList().get(1).getTime());
		Assert.assertEquals(15L, sinkPlugin.getList().get(2).getTime());

		// check for empty window
		Assert.assertEquals(Double.NaN, sinkPlugin.getList().get(1).getDoubleValue(), 0.001d);

		// the third created item is not available in the sink as its window was not closed when the controller terminated
	}

	@Test
	public void testTimestampTimeUnitMilli() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> listReader = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		listReader.addObject(this.createNDTSP(1000000L, 3, OP_SIGNATURE_A));
		listReader.addObject(this.createNDTSP(13000000L, 13, OP_SIGNATURE_A));
		listReader.addObject(this.createNDTSP(16000000L, 16, OP_SIGNATURE_A));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "5");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "MILLISECONDS");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
		final TimeSeriesPointAggregatorFilter aggregator = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, controller);

		// SINK
		final ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(),
				controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		// CONNECTION
		controller.connect(listReader, ListReader.OUTPUT_PORT_NAME, aggregator, TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		controller.connect(aggregator, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertEquals(0, sinkPlugin.getList().size());
		controller.run();

		Assert.assertEquals(3, sinkPlugin.getList().size());

		// check for correct windows
		Assert.assertEquals(5999999L, sinkPlugin.getList().get(0).getTime());
		Assert.assertEquals(10999999L, sinkPlugin.getList().get(1).getTime());
		Assert.assertEquals(15999999L, sinkPlugin.getList().get(2).getTime());

		// check for empty window
		Assert.assertEquals(Double.NaN, sinkPlugin.getList().get(1).getDoubleValue(), 0.001d);

		// the third created item is not available in the sink as its window was not closed when the controller terminated
	}
}
