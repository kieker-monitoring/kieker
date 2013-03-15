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

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.IForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.IForecaster;

/**
 * Filters every incoming events by a given list of names
 * 
 * @author Tillmann Carlos Bielefeld, Andre van Hoorn
 * 
 */
@Plugin(name = "Forecast Filter", outputPorts = {
	@OutputPort(eventTypes = { IForecastResult.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECAST),
	@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT) },
		configuration = {
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, defaultValue = "1000"),
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT, defaultValue = "MILLISECONDS"),
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, defaultValue = "MEAN")
		})
public class ForecastingFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";
	public static final String INPUT_PORT_NAME_TRIGGER = "trigger";

	public static final String OUTPUT_PORT_NAME_FORECAST = "forecast";
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";

	public static final String CONFIG_PROPERTY_DELTA_TIME = "deltatime";
	public static final String CONFIG_PROPERTY_DELTA_UNIT = "deltaunit";
	public static final String CONFIG_PROPERTY_FC_METHOD = "fcmethod";

	private static final Object TRIGGER = new Object();

	private final ITimeSeries<Double> timeSeriesWindow;
	private final ForecastMethod forecastMethod;
	private NamedDoubleTimeSeriesPoint lastPoint;

	private final IForecaster<Double> forecaster;

	public ForecastingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		final long deltat = configuration.getLongProperty(CONFIG_PROPERTY_DELTA_TIME);
		final TimeUnit tunit = TimeUnit.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_DELTA_UNIT));

		this.timeSeriesWindow = new TimeSeries<Double>(new Date(), deltat, tunit);

		// TODO select the method based on the configuration
		this.forecastMethod = ForecastMethod.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_FC_METHOD));
		this.forecaster = this.forecastMethod.getForecaster(this.timeSeriesWindow);
	}

	@Deprecated
	public ForecastingFilter(final Configuration configuration) {
		this(configuration, null);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = ForecastingFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputEvent(final NamedDoubleTimeSeriesPoint input) {
		this.timeSeriesWindow.append(input.getValue());

		this.lastPoint = input;

		// TODO: have this method triggered if no trigger port is given!
		this.inputTrigger(TRIGGER);
	}

	@InputPort(eventTypes = { Object.class }, name = ForecastingFilter.INPUT_PORT_NAME_TRIGGER)
	public void inputTrigger(final Object trigger) {

		// TODO read the steps from config
		final IForecastResult<Double> result = this.forecaster.forecast(1);
		super.deliver(OUTPUT_PORT_NAME_FORECAST, result);

		final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
				this.lastPoint.getName(),
				result.getForecast().getPoints().get(0).getValue(),
				this.lastPoint.getValue(),
				this.lastPoint.getTime());
		super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
	}

}
