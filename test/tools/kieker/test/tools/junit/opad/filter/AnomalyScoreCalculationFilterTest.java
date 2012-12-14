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
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ITimeSeriesPoint;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class AnomalyScoreCalculationFilterTest {

	private ForecastingFilter forecasting;
	private AnalysisController controller;
	private ListCollectionFilter<IForecastResult<Double>> sinkPlugin;

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		final Configuration config = new Configuration();
		config.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		config.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MINUTES");
		config.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");

		this.forecasting = new ForecastingFilter(config);

		this.sinkPlugin = new ListCollectionFilter<IForecastResult<Double>>(new Configuration());
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

		this.controller = new AnalysisController();
		this.controller.registerFilter(this.forecasting);
		this.controller.registerFilter(this.sinkPlugin);
		this.controller.connect(this.forecasting,
				ForecastingFilter.OUTPUT_PORT_NAME_FORECAST, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
	}

	@Test
	public void testFilterOnly() {
		this.forecasting.inputEvent(new NamedDoubleTimeSeriesPoint(new Date(), 0.3, "a"));
		this.forecasting.inputEvent(new NamedDoubleTimeSeriesPoint(new Date(), 0.4, "a"));
		this.forecasting.inputEvent(new NamedDoubleTimeSeriesPoint(new Date(), 0.5, "a"));

		Assert.assertEquals(3, this.sinkPlugin.getList().size());
		final IForecastResult<Double> lastresult = this.sinkPlugin.getList().get(2);
		final ITimeSeriesPoint<Double> nextMeanFC = lastresult.getForecast().getPoints().get(0);
		Assert.assertEquals(new Double(0.4), nextMeanFC.getValue());
	}

}
