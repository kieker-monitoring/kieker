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

package kieker.test.tools.junit.opad.filter;

import java.util.List;

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
 * @author Tom Frotscher, Teerat Pitakrat
 * @since 1.10
 * 
 */
public class TimeSeriesPointAggregatorTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";
	private static final String OP_SIGNATURE_C = "c.C.opC";
	private static final String OP_SIGNATURE_D = "d.D.opD";

	private static final double EPSILON = 1e-8;

	/**
	 * Creates an instance of this class.
	 */
	public TimeSeriesPointAggregatorTest() {
		// empty default constructor
	}

	@Test
	public void testAggregatorOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(664L, 1000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(665L, 2000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(664L, 1000.0, OP_SIGNATURE_C));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(668L, 5500.0, OP_SIGNATURE_C));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(674L, 2000.0, OP_SIGNATURE_B));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(674L, 3000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(684L, 4000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(674L, 2000.0, OP_SIGNATURE_C));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(684L, 5000.0, OP_SIGNATURE_B));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(687L, 1000.0, OP_SIGNATURE_B));
		// Skipped two timespans for application A, so two empty points should be created
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(714L, 6000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(724L, 7000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(694L, 5000.0, OP_SIGNATURE_B));
		// Skipped one timespans for application B, so one empty point should be created
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(714L, 5000.0, OP_SIGNATURE_B));

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

		listReader.addObject(new NamedDoubleTimeSeriesPoint(1L, 3.0, OP_SIGNATURE_A));
		listReader.addObject(new NamedDoubleTimeSeriesPoint(13L, 13.0, OP_SIGNATURE_A));
		listReader.addObject(new NamedDoubleTimeSeriesPoint(16L, 16.0, OP_SIGNATURE_A));

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

		listReader.addObject(new NamedDoubleTimeSeriesPoint(1000000L, 3.0, OP_SIGNATURE_A));
		listReader.addObject(new NamedDoubleTimeSeriesPoint(13000000L, 13.0, OP_SIGNATURE_A));
		listReader.addObject(new NamedDoubleTimeSeriesPoint(16000000L, 16.0, OP_SIGNATURE_A));

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

	@Test
	public void testRealtimeProcessing() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisController controller = new AnalysisController();

		// READER
		final Configuration readerAggregationConfiguration = new Configuration();
		final ListReader<NamedDoubleTimeSeriesPoint> theReaderAggregator = new ListReader<NamedDoubleTimeSeriesPoint>(readerAggregationConfiguration,
				controller);

		/**
		 * <pre>
		 * 
		 *       1.[1,10]        5.[11,20]         9.[21,30]          13.[31,40]
		 * A -[1--------10]-[11--------18---]-[----------------]-[----------------]-[------------
		 * 
		 *                 4.[8,17]            8.[18,27]        12.[28,37]         16.[38,47]    
		 * B -------[8---------12--------]-[------------28]-[----------------]-[----------------]
		 * 
		 *            3.[5,14]          7.[15,24]         11.[25,34]         15.[35,44]
		 * C ----[5--------------]-[---18----------]-[----------------]-[----------------]-[---48
		 * 
		 *            2.[5,14]          6.[15,24]         10.[25,34]         14.[35,44]
		 * D ----[5--------------]-[---18----------]-[----------------]-[----------------]-[-----
		 * 
		 * 
		 * Description:
		 * A -[1--------10]--- = A has two monitoring records at timestamp 1 and 10
		 * [ = Begining of aggregation window
		 * ] = End of aggregation window
		 * 
		 * X.[Y,Z] = aggregation window
		 * X = expected order of aggregated window from the filter
		 * Y = first timestamp
		 * Z = last timestamp
		 * 
		 * </pre>
		 */

		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(1L, 1000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(5L, 1000.0, OP_SIGNATURE_C));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(5L, 1000.0, OP_SIGNATURE_D));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(8L, 2000.0, OP_SIGNATURE_B));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(10L, 2000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(11L, 3000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(12L, 4000.0, OP_SIGNATURE_B));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(18L, 5000.0, OP_SIGNATURE_C));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(18L, 5000.0, OP_SIGNATURE_D));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(18L, 5000.0, OP_SIGNATURE_A));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(28L, 6000.0, OP_SIGNATURE_B));
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(10L, 7000.0, OP_SIGNATURE_C)); // should be ignored by the filter
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(17L, 7000.0, OP_SIGNATURE_B)); // should be ignored by the filter
		theReaderAggregator.addObject(new NamedDoubleTimeSeriesPoint(48L, 7000.0, OP_SIGNATURE_C));

		// AGGREGATIONFILTER
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "10");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "NANOSECONDS");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMESCOPE,
				TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_VALUE_AGGREGATION_TIMESCOPE_GLOBAL);
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

		final List<NamedDoubleTimeSeriesPoint> sinkList = sinkPlugin.getList();
		Assert.assertEquals(16, sinkList.size());
		// Expected: (1000 + 2000) / 2 = 1500 Application A
		Assert.assertEquals(OP_SIGNATURE_A, sinkList.get(0).getName());
		Assert.assertEquals(1500.0, sinkList.get(0).getDoubleValue(), EPSILON);
		// Expected: 1000 Application D
		Assert.assertEquals(OP_SIGNATURE_D, sinkList.get(1).getName());
		Assert.assertEquals(1000.0, sinkList.get(1).getDoubleValue(), EPSILON);
		// Expected: 1000 Application C
		Assert.assertEquals(OP_SIGNATURE_C, sinkList.get(2).getName());
		Assert.assertEquals(1000.0, sinkList.get(2).getDoubleValue(), EPSILON);
		// Expected: (2000+4000) / 2 = 3000 Application B
		Assert.assertEquals(OP_SIGNATURE_B, sinkList.get(3).getName());
		Assert.assertEquals(3000.0, sinkList.get(3).getDoubleValue(), EPSILON);
		// Expected: (3000+5000) / 2 = 4000 Application A
		Assert.assertEquals(OP_SIGNATURE_A, sinkList.get(4).getName());
		Assert.assertEquals(4000.0, sinkList.get(4).getDoubleValue(), EPSILON);
		// Expected: 5000 Application D
		Assert.assertEquals(OP_SIGNATURE_D, sinkList.get(5).getName());
		Assert.assertEquals(5000.0, sinkList.get(5).getDoubleValue(), EPSILON);
		// Expected: 5000 Application C
		Assert.assertEquals(OP_SIGNATURE_C, sinkList.get(6).getName());
		Assert.assertEquals(5000.0, sinkList.get(6).getDoubleValue(), EPSILON);
		// Expected: NaN Application B
		Assert.assertEquals(OP_SIGNATURE_B, sinkList.get(7).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(7).getDoubleValue(), EPSILON);
		// Expected: NaN Application A
		Assert.assertEquals(OP_SIGNATURE_A, sinkList.get(8).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(8).getDoubleValue(), EPSILON);
		// Expected: NaN Application D
		Assert.assertEquals(OP_SIGNATURE_D, sinkList.get(9).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(9).getDoubleValue(), EPSILON);
		// Expected: NaN Application C
		Assert.assertEquals(OP_SIGNATURE_C, sinkList.get(10).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(10).getDoubleValue(), EPSILON);
		// Expected: NaN Application B
		Assert.assertEquals(OP_SIGNATURE_B, sinkList.get(11).getName());
		Assert.assertEquals(6000.0, sinkList.get(11).getDoubleValue(), EPSILON);
		// Expected: NaN Application A
		Assert.assertEquals(OP_SIGNATURE_A, sinkList.get(12).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(12).getDoubleValue(), EPSILON);
		// Expected: NaN Application D
		Assert.assertEquals(OP_SIGNATURE_D, sinkList.get(13).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(13).getDoubleValue(), EPSILON);
		// Expected: NaN Application C
		Assert.assertEquals(OP_SIGNATURE_C, sinkList.get(14).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(14).getDoubleValue(), EPSILON);
		// Expected: NaN Application B
		Assert.assertEquals(OP_SIGNATURE_B, sinkList.get(15).getName());
		Assert.assertEquals(Double.NaN, sinkList.get(15).getDoubleValue(), EPSILON);
	}
}
