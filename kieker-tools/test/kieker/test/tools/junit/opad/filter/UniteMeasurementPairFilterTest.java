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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.UniteMeasurementPairFilter;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.model.IForecastMeasurementPair;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Checks if the forecasts are assigned to the correct real values. Also checks, if a dummy is created for the first real value, that can not have a calculated
 * forecast.
 * 
 * @author Tom Frotscher, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class UniteMeasurementPairFilterTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";

	private AnalysisController controller;

	private ListCollectionFilter<ForecastMeasurementPair> sinkPlugin;

	public UniteMeasurementPairFilterTest() {
		// empty default constructor
	}

	// HelperMethods UniteFilter
	private NamedDoubleTimeSeriesPoint createNDTSP(final long d, final String signature, final double value) {
		return new NamedDoubleTimeSeriesPoint(d, value, signature);
	}

	private List<NamedDoubleTimeSeriesPoint> createInputEventSetUnite() {
		final List<NamedDoubleTimeSeriesPoint> retList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		retList.add(this.createNDTSP(0L, OP_SIGNATURE_A, 0.3));
		retList.add(this.createNDTSP(5L, OP_SIGNATURE_A, 0.4));
		retList.add(this.createNDTSP(10L, OP_SIGNATURE_A, 0.5));
		retList.add(this.createNDTSP(15L, OP_SIGNATURE_A, 0.9));
		retList.add(this.createNDTSP(0L, OP_SIGNATURE_B, 0.7));
		retList.add(this.createNDTSP(8L, OP_SIGNATURE_B, 0.3));
		retList.add(this.createNDTSP(10L, OP_SIGNATURE_B, 0.1));
		retList.add(this.createNDTSP(17L, OP_SIGNATURE_B, 0.97));
		return retList;
	}

	private List<IForecastMeasurementPair> createInputEventSetUniteForecast() {
		final List<IForecastMeasurementPair> retList = new ArrayList<IForecastMeasurementPair>();
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_A, 0.35, 1.0, 5L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_A, 0.45, 1.0, 10L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_A, 0.55, 1.0, 15L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_A, 0.95, 1.0, 20L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_B, 0.31, 1.0, 8L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_B, 0.46, 1.0, 10L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_B, 0.55, 1.0, 17L));
		retList.add(new ForecastMeasurementPair(OP_SIGNATURE_B, 0.95, 1.0, 20L));
		return retList;
	}

	/**
	 * Set up for the VariateUniteFMPFilterTest.
	 * 
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// READER TSPoints
		final Configuration readerUniteConfigurationTS = new Configuration();
		readerUniteConfigurationTS.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.FALSE.toString());
		final ListReader<NamedDoubleTimeSeriesPoint> theReaderUniteTSPoints = new ListReader<NamedDoubleTimeSeriesPoint>(readerUniteConfigurationTS,
				this.controller);
		theReaderUniteTSPoints.addAllObjects(this.createInputEventSetUnite());

		// READER Forecasts
		final Configuration readerUniteConfigurationForecast = new Configuration();
		readerUniteConfigurationForecast.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.FALSE.toString());
		final ListReader<IForecastMeasurementPair> theReaderUniteForecast = new ListReader<IForecastMeasurementPair>(readerUniteConfigurationForecast,
				this.controller);
		theReaderUniteForecast.addAllObjects(this.createInputEventSetUniteForecast());

		// UniteMeasurementPair Filter
		final Configuration uniteConfiguration = new Configuration();
		final UniteMeasurementPairFilter unite = new UniteMeasurementPairFilter(uniteConfiguration, this.controller);

		// SINK 1
		this.sinkPlugin = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(), this.controller);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

		// CONNECTION
		this.controller.connect(theReaderUniteTSPoints, ListReader.OUTPUT_PORT_NAME, unite, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);
		this.controller.connect(theReaderUniteForecast, ListReader.OUTPUT_PORT_NAME, unite, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);
		this.controller.connect(unite, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Test of the VariateUniteFMPFilter. The measurement values and the forecast values have to be brought together correctly. Therefore, the measurements and
	 * forecasts with corresponding time stamps have to be brought together if they are from the same application.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Test
	public void testUniteOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		this.controller.run();

		Assert.assertEquals(8, this.sinkPlugin.getList().size());

		Assert.assertEquals(0.3, this.sinkPlugin.getList().get(0).getValue(), 1e-5);
		Assert.assertEquals(0.4, this.sinkPlugin.getList().get(1).getValue(), 1e-5);
		Assert.assertEquals(0.5, this.sinkPlugin.getList().get(2).getValue(), 1e-5);
		Assert.assertEquals(0.9, this.sinkPlugin.getList().get(3).getValue(), 1e-5);
		Assert.assertEquals(0.7, this.sinkPlugin.getList().get(4).getValue(), 1e-5);
		Assert.assertEquals(0.3, this.sinkPlugin.getList().get(5).getValue(), 1e-5);
		Assert.assertEquals(0.1, this.sinkPlugin.getList().get(6).getValue(), 1e-5);
		Assert.assertEquals(0.97, this.sinkPlugin.getList().get(7).getValue(), 1e-5);
		Assert.assertEquals(0.3, this.sinkPlugin.getList().get(0).getForecasted(), 1e-5);
		Assert.assertEquals(0.35, this.sinkPlugin.getList().get(1).getForecasted(), 1e-5);
		Assert.assertEquals(0.45, this.sinkPlugin.getList().get(2).getForecasted(), 1e-5);
		Assert.assertEquals(0.55, this.sinkPlugin.getList().get(3).getForecasted(), 1e-5);
		Assert.assertEquals(0.7, this.sinkPlugin.getList().get(4).getForecasted(), 1e-5);
		Assert.assertEquals(0.31, this.sinkPlugin.getList().get(5).getForecasted(), 1e-5);
		Assert.assertEquals(0.46, this.sinkPlugin.getList().get(6).getForecasted(), 1e-5);
		Assert.assertEquals(0.55, this.sinkPlugin.getList().get(7).getForecasted(), 1e-5);
	}
}
