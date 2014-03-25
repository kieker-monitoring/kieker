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

package kieker.tools.opad.filter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.opad.ForecastMeasurementPair;
import kieker.common.record.opad.IForecastMeasurementPair;
import kieker.common.record.opad.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.IForecaster;

/**
 * Computes a forecast for every incoming measurement from different applications.
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
@Plugin(name = "Forecast Filter", outputPorts = {
	@OutputPort(eventTypes = { IForecastResult.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECAST),
	@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT) },
		configuration = {
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, defaultValue = "1000"),
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT, defaultValue = "MILLISECONDS"),
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, defaultValue = "MEAN"),
			@Property(name = ForecastingFilter.CONFIG_PROPERTY_TS_WINDOW_CAPACITY, defaultValue = "60")
		})
public class ForecastingFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the measurements.
	 */
	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

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
	/** Name of the property determining the capacity of the timeseries window. Take value <= 0 for infinite buffersize */
	public static final String CONFIG_PROPERTY_TS_WINDOW_CAPACITY = "tswcapacity";

	private final ConcurrentHashMap<String, ITimeSeries<Double>> applicationForecastingWindow;
	private final int timeSeriesWindowCapacity;
	private final ForecastMethod forecastMethod;

	private final long deltat;
	private final TimeUnit tunit;

	private IForecaster<Double> forecaster;

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

		this.applicationForecastingWindow = new ConcurrentHashMap<String, ITimeSeries<Double>>();
		this.deltat = configuration.getLongProperty(CONFIG_PROPERTY_DELTA_TIME);
		this.tunit = TimeUnit.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_DELTA_UNIT));
		this.timeSeriesWindowCapacity = configuration.getIntProperty(CONFIG_PROPERTY_TS_WINDOW_CAPACITY);

		this.forecastMethod = ForecastMethod.valueOf(configuration
				.getStringProperty(CONFIG_PROPERTY_FC_METHOD));
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_DELTA_TIME, Long.toString(this.deltat));
		configuration.setProperty(CONFIG_PROPERTY_DELTA_UNIT, this.tunit.name());
		configuration.setProperty(CONFIG_PROPERTY_FC_METHOD, this.forecastMethod.name());
		configuration.setProperty(CONFIG_PROPERTY_TS_WINDOW_CAPACITY, Integer.toString(this.timeSeriesWindowCapacity));
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
		if (this.checkInitialization(input.getName())) {
			this.processInput(input, input.getTime(), input.getName());
		} else {
			// Initialization of the forecasting variables for a new application
			this.applicationForecastingWindow.put(input.getName(),
					new TimeSeries<Double>(System.currentTimeMillis(), this.deltat, this.tunit, this.timeSeriesWindowCapacity));
			this.processInput(input, input.getTime(), input.getName());
		}

	}

	/**
	 * Calculating the Forecast and delivers it.
	 * 
	 * @param input
	 *            Incoming measurement
	 * @param timestamp
	 *            Timestamp of the measurement
	 * @param name
	 *            Name of the application of the measurement
	 */
	public void processInput(final NamedDoubleTimeSeriesPoint input, final long timestamp, final String name) {
		final ITimeSeries<Double> actualWindow = this.applicationForecastingWindow.get(name);
		actualWindow.append(input.getValue());
		this.forecaster = this.forecastMethod.getForecaster(actualWindow);
		final IForecastResult<Double> result = this.forecaster.forecast(1);
		super.deliver(OUTPUT_PORT_NAME_FORECAST, result);

		final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
				name,
				result.getForecast().getPoints().get(0).getValue(),
				input.getValue(),
				timestamp);
		super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
	}

	/**
	 * Checks if the current application is already known to this filter.
	 * 
	 * @param name
	 *            Application name
	 */
	private boolean checkInitialization(final String name) {
		return this.applicationForecastingWindow.containsKey(name);
	}

}
