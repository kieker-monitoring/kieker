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
import java.util.concurrent.TimeUnit;

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
import kieker.tools.opad.filter.UniteMeasurementPairFilter;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.ForecastResult;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * 
 * @author Tom Frotscher
 * 
 */
public class UniteMeasurementPairFilterTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";

	private AnalysisController controller;

	// Variables UniteFilter
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderUniteTSPoints;
	private ListReader<IForecastResult<Double>> theReaderUniteForecast;
	private UniteMeasurementPairFilter unite;
	private ListCollectionFilter<ForecastMeasurementPair> sinkPlugin;

	// HelperMethods UniteFilter

	private NamedDoubleTimeSeriesPoint createNDTSP(final Date d, final String signature, final double value) {
		final NamedDoubleTimeSeriesPoint r = new NamedDoubleTimeSeriesPoint(d, value, signature);
		return r;
	}

	private IForecastResult<Double> createFR(final Date d, final double value) {
		final TimeSeries<Double> ts = new TimeSeries<Double>(d, 1, TimeUnit.MICROSECONDS);
		ts.append(value);
		final IForecastResult<Double> r = new ForecastResult(ts, null);
		return r;
	}

	private List<NamedDoubleTimeSeriesPoint> createInputEventSetUnite() {
		final List<NamedDoubleTimeSeriesPoint> retList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		retList.add(this.createNDTSP(new Date(0L), OP_SIGNATURE_A, 0.3));
		retList.add(this.createNDTSP(new Date(5L), OP_SIGNATURE_A, 0.4));
		retList.add(this.createNDTSP(new Date(10L), OP_SIGNATURE_A, 0.5));
		retList.add(this.createNDTSP(new Date(15L), OP_SIGNATURE_A, 0.9));
		return retList;
	}

	private List<IForecastResult<Double>> createInputEventSetUniteForecast() {
		final List<IForecastResult<Double>> retList = new ArrayList<IForecastResult<Double>>();
		retList.add(this.createFR(new Date(5L), 0.35));
		retList.add(this.createFR(new Date(10L), 0.45));
		retList.add(this.createFR(new Date(15L), 0.55));
		retList.add(this.createFR(new Date(20L), 0.95));
		return retList;
	}

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// READER TSPoints
		final Configuration readerUniteConfigurationTS = new Configuration();
		readerUniteConfigurationTS.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderUniteTSPoints = new ListReader<NamedDoubleTimeSeriesPoint>(readerUniteConfigurationTS, this.controller);
		this.theReaderUniteTSPoints.addAllObjects(this.createInputEventSetUnite());

		// READER Forecasts
		final Configuration readerUniteConfigurationForecast = new Configuration();
		readerUniteConfigurationForecast.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderUniteForecast = new ListReader<IForecastResult<Double>>(readerUniteConfigurationForecast, this.controller);
		this.theReaderUniteForecast.addAllObjects(this.createInputEventSetUniteForecast());

		// UniteMeasurementPair Filter
		final Configuration uniteConfiguration = new Configuration();
		uniteConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		uniteConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MILLISECONDS");
		uniteConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");
		this.unite = new UniteMeasurementPairFilter(uniteConfiguration, this.controller);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(), this.controller);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

		// CONNECTION
		this.controller.connect(this.theReaderUniteTSPoints, ListReader.OUTPUT_PORT_NAME, this.unite, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(this.theReaderUniteForecast, ListReader.OUTPUT_PORT_NAME, this.unite, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);
		this.controller.connect(this.unite,
				UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, this.sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
	}

	// Test for the ForeCasting Filter
	@Test
	public void testUniteOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(2000);
		thread.terminate();

		Assert.assertEquals(4, this.sinkPlugin.getList().size());
		System.out.println("--- Paar 1 ---");
		final double r1 = this.sinkPlugin.getList().get(0).getValue();
		final double f1 = this.sinkPlugin.getList().get(0).getForecasted();
		System.out.println(r1 + " und " + f1 + " <-- Dummy");

		System.out.println("--- Paar 2 ---");
		final double r2 = this.sinkPlugin.getList().get(1).getValue();
		final double f2 = this.sinkPlugin.getList().get(1).getForecasted();
		System.out.println(r2 + " und " + f2);

		System.out.println("--- Paar 3 ---");
		final double r3 = this.sinkPlugin.getList().get(2).getValue();
		final double f3 = this.sinkPlugin.getList().get(2).getForecasted();
		System.out.println(r3 + " und " + f3);

		System.out.println("--- Paar 4 ---");
		final double r4 = this.sinkPlugin.getList().get(3).getValue();
		final double f4 = this.sinkPlugin.getList().get(3).getForecasted();
		System.out.println(r4 + " und " + f4);

	}

}
