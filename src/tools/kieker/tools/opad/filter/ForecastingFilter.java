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
 * Computes a forecast for every incoming measurement.
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

	/**
	 * Name of the input port receiving the measurements.
	 */
	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	/**
	 * Name of the input port receiving the triggers.
	 */
	public static final String INPUT_PORT_NAME_TRIGGER = "trigger";

	/**
	 * Name of the output port delivering the forecasts.
	 */
	public static final String OUTPUT_PORT_NAME_FORECAST = "forecast";

	/**
	 * Name of the output port delivering the measurement-forecast-pairs.
	 */
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";

	/** Name of the property determining the deltatime. */
	public static final String CONFIG_PROPERTY_DELTA_TIME = "deltatime";
	/** Name of the property determining the timeunit of the deltatime. */
	public static final String CONFIG_PROPERTY_DELTA_UNIT = "deltaunit";
	/** Name of the property determining the forecasting method. */
	public static final String CONFIG_PROPERTY_FC_METHOD = "fcmethod";

	private static final Object TRIGGER = new Object();

	private final ITimeSeries<Double> timeSeriesWindow;
	private final ForecastMethod forecastMethod;
	private NamedDoubleTimeSeriesPoint lastPoint;

	private final long deltat;
	private final TimeUnit tunit;

	private final IForecaster<Double> forecaster;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param configuration
	 *            Configuration of this component
	 * @param projectContext
	 *            ProjectContext of this component
	 */
	public ForecastingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.deltat = configuration.getLongProperty(CONFIG_PROPERTY_DELTA_TIME);
		this.tunit = TimeUnit.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_DELTA_UNIT));

		this.timeSeriesWindow = new TimeSeries<Double>(System.currentTimeMillis(), this.deltat, this.tunit);

		this.forecastMethod = ForecastMethod.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_FC_METHOD));
		this.forecaster = this.forecastMethod.getForecaster(this.timeSeriesWindow);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_DELTA_TIME, Long.toString(this.deltat));
		configuration.setProperty(CONFIG_PROPERTY_DELTA_UNIT, this.tunit.name());
		configuration.setProperty(CONFIG_PROPERTY_FC_METHOD, this.forecastMethod.name());
		return configuration;
	}

	/**
	 * Represents the input port for measurements.
	 * 
	 * @param input
	 *            Incoming measurements
	 */
	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = ForecastingFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputEvent(final NamedDoubleTimeSeriesPoint input) {
		this.timeSeriesWindow.append(input.getValue());

		this.lastPoint = input;

		this.inputTrigger(TRIGGER);
	}

	/**
	 * Represents the input port for triggers. Delivers the forecasting result and a forecasting-measurement-pair.
	 * 
	 * @param trigger
	 *            Incoming trigger
	 */
	@InputPort(eventTypes = { Object.class }, name = ForecastingFilter.INPUT_PORT_NAME_TRIGGER)
	public void inputTrigger(final Object trigger) {

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
