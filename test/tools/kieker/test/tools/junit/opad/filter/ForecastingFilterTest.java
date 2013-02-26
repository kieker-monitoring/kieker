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
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ITimeSeriesPoint;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * 
 * @author Tillmann Carlos Bielefeld, Tom Frotscher
 * 
 */
public class ForecastingFilterTest {

	private AnalysisController controller;

	// Variables ForecastingFilter
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderForecast;
	private ForecastingFilter forecasting;
	private ListCollectionFilter<IForecastResult<Double>> sinkPlugin;
	private static final String OP_SIGNATURE_A = "a.A.opA";

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

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// READER
		final Configuration readerForecastConfiguration = new Configuration();
		readerForecastConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderForecast = new ListReader<NamedDoubleTimeSeriesPoint>(readerForecastConfiguration);
		this.theReaderForecast.addAllObjects(this.createInputEventSetForecast());
		this.controller.registerReader(this.theReaderForecast);

		// FORECASTINGFILTER
		final Configuration forecastConfiguration = new Configuration();
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MILLISECONDS");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");
		this.forecasting = new ForecastingFilter(forecastConfiguration);
		this.controller.registerFilter(this.forecasting);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<IForecastResult<Double>>(new Configuration());
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.controller.registerFilter(this.sinkPlugin);

		// CONNECTION
		this.controller.connect(this.theReaderForecast, ListReader.OUTPUT_PORT_NAME, this.forecasting, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.forecasting,
				ForecastingFilter.OUTPUT_PORT_NAME_FORECAST, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
	}

	// Test for the ForeCasting Filter
	@Test
	public void testForecastingOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(1000);
		thread.terminate();

		Assert.assertEquals(3, this.sinkPlugin.getList().size());
		final IForecastResult<Double> lastresult = this.sinkPlugin.getList().get(2);
		final ITimeSeriesPoint<Double> nextMeanFC = lastresult.getForecast().getPoints().get(0);
		Assert.assertEquals(new Double(0.4), nextMeanFC.getValue());

	}

}
