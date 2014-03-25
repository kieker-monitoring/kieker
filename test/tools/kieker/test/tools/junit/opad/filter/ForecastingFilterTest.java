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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.opad.ForecastMeasurementPair;
import kieker.common.record.opad.NamedDoubleTimeSeriesPoint;
import kieker.tools.opad.filter.ForecastingFilter;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Basically compares the results of the forecaster with previous manually calculated forecasted results.
 * Currently for the mean forecaster.
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
public class ForecastingFilterTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";
	private static final String OP_SIGNATURE_C = "c.C.opC";

	private AnalysisController controller;

	// Variables ForecastingFilter
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderForecast;
	private ForecastingFilter forecasting;
	private ListCollectionFilter<ForecastMeasurementPair> sinkPlugin;

	/**
	 * Creates an instance of this class.
	 */
	public ForecastingFilterTest() {
		// empty default constructor
	}

	// HelperMethods ForecastingFilter
	private NamedDoubleTimeSeriesPoint createNDTSP(final String signature, final double value) {
		final NamedDoubleTimeSeriesPoint r = new NamedDoubleTimeSeriesPoint(System.currentTimeMillis(), value, signature);
		return r;
	}

	private List<NamedDoubleTimeSeriesPoint> createInputEventSetForecast() {
		final List<NamedDoubleTimeSeriesPoint> retList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.3));
		retList.add(this.createNDTSP(OP_SIGNATURE_B, 0.4));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.5));
		retList.add(this.createNDTSP(OP_SIGNATURE_C, 0.5));
		retList.add(this.createNDTSP(OP_SIGNATURE_B, 0.3));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.4));
		retList.add(this.createNDTSP(OP_SIGNATURE_B, 0.5));
		return retList;
	}

	/**
	 * Sets up the controller and configuration for the test of the VariateForecastingFilter.
	 * 
	 * @throws IllegalStateException
	 *             If illegal State
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// READER
		final Configuration readerForecastConfiguration = new Configuration();
		readerForecastConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderForecast = new ListReader<NamedDoubleTimeSeriesPoint>(readerForecastConfiguration, this.controller);
		this.theReaderForecast.addAllObjects(this.createInputEventSetForecast());

		// FORECASTINGFILTER
		final Configuration forecastConfiguration = new Configuration();
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MILLISECONDS");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");
		this.forecasting = new ForecastingFilter(forecastConfiguration, this.controller);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(), this.controller);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

		// CONNECTION
		this.controller.connect(this.theReaderForecast, ListReader.OUTPUT_PORT_NAME, this.forecasting, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.forecasting,
				ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Test of the Forecasting with incoming measurements of different Applications. This test case is successful for the MEANJAVA Forecaster.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Test
	public void testForecastingOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(1000);
		thread.terminate();

		Assert.assertEquals(7, this.sinkPlugin.getList().size());
		// Expected: 0.3 - 0.3 (dummy) Application A
		Assert.assertEquals(Double.valueOf(0.3), this.sinkPlugin.getList().get(0).getForecasted());
		// Expected: 0.4 - 0.4 (dummy) Application B
		Assert.assertEquals(Double.valueOf(0.4), this.sinkPlugin.getList().get(1).getForecasted());
		// Expected: (0.3 + 0.5) / 2 = 0.4 Application A
		Assert.assertEquals(Double.valueOf(0.4), this.sinkPlugin.getList().get(2).getForecasted());
		// Expected: 0.5 = 0.5 (dummy) Application C
		Assert.assertEquals(Double.valueOf(0.5), this.sinkPlugin.getList().get(3).getForecasted());
		// Expected: (0.4 + 0.3) / 2 = 0.35 Application B
		Assert.assertEquals(Double.valueOf(0.35), this.sinkPlugin.getList().get(4).getForecasted());
		// Expected: (0.3 + 0.5 + 0.4) / 3 = 0.4 Application A
		Assert.assertEquals(Double.valueOf(0.4), this.sinkPlugin.getList().get(5).getForecasted());
		// Expected: (0.4 + 0.3 + 0.5) / 3 = 0.4 Application B
		Assert.assertEquals(Double.valueOf(0.4), this.sinkPlugin.getList().get(6).getForecasted());

	}

}
