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

package kieker.tools.opad.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.IForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * This Filter brings a Measurement Point and a corresponding Forecasting value together.
 * 
 * @author Tom Frotscher
 * 
 */
@Plugin(name = "UniteMeasurementPair Filter", outputPorts = {
	@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT) })
public class UniteMeasurementPairFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";
	public static final String INPUT_PORT_NAME_FORECAST = "forecast";

	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";

	private final List<IForecastResult<Double>> forecastValues;
	private volatile boolean firstTSPoint = true;

	public UniteMeasurementPairFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.forecastValues = Collections.synchronizedList(new ArrayList<IForecastResult<Double>>());
	}

	@Deprecated
	public UniteMeasurementPairFilter(final Configuration configuration) {
		this(configuration, null);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputTSPoint(final NamedDoubleTimeSeriesPoint input) {
		// First TSPoint have no corresponding Forecastvalue, cause first Forecastvale is one Step in the Future.
		// For further processing we need to give this Point a Forecast. We use the TSPoint itsel as Dummy for now
		if (this.firstTSPoint) {
			this.firstTSPoint = false;
			final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
					input.getName(),
					input.getValue(),
					input.getValue(),
					input.getTime());
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
		} else {
			// Bring together the currently incoming TSPoint with the corresponding ForecastValue
			final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
					input.getName(),
					this.forecastValues.get(0).getForecast().getPoints().get(0).getValue(),
					input.getValue(),
					input.getTime());
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
			this.forecastValues.remove(0);
		}
	}

	@InputPort(eventTypes = { IForecastResult.class }, name = UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST)
	public void inputForecastValue(final IForecastResult<Double> value) {
		this.forecastValues.add(value);
	}

}
