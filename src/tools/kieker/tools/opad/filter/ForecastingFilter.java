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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.plugin.AbstractUpdateableFilterPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.model.IForecastMeasurementPair;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.IForecaster;

/**
 * Computes a forecast for every incoming measurement from different applications.
 *
 * @since 1.10
 * @author Tom Frotscher, Thomas Duellmann, Tobias Rudolph
 *
 */
@Plugin(name = "Forecast Filter", outputPorts = {
		@OutputPort(eventTypes = { IForecastResult.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECAST),
		@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT),
		@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_MEASURED) },
		configuration = {
		@Property(name = ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, defaultValue = "1000"),
		@Property(name = ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, defaultValue = "MILLISECONDS"),
		@Property(name = ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, defaultValue = "MEAN", updateable = true),
		@Property(name = ForecastingFilter.CONFIG_PROPERTY_NAME_TS_WINDOW_CAPACITY, defaultValue = "60"),
		@Property(name = ForecastingFilter.CONFIG_PROPERTY_NAME_FC_CONFIDENCE, defaultValue = "0")
})
public class ForecastingFilter extends AbstractUpdateableFilterPlugin {

	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	public static final String OUTPUT_PORT_NAME_FORECAST = "forecast";
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_MEASURED = "forecastedandmeasured";

	public static final String CONFIG_PROPERTY_NAME_DELTA_TIME = "deltatime";
	public static final String CONFIG_PROPERTY_NAME_DELTA_UNIT = "deltaunit";
	public static final String CONFIG_PROPERTY_NAME_FC_METHOD = "fcmethod";
	public static final String CONFIG_PROPERTY_NAME_TS_WINDOW_CAPACITY = "tswcapacity";
	public static final String CONFIG_PROPERTY_NAME_FC_CONFIDENCE = "confidence";

	private final ConcurrentHashMap<String, ITimeSeries<Double>> applicationForecastingWindow;
	private final ConcurrentHashMap<String, ForecastMeasurementPair> previousFCPair;

	private AtomicInteger timeSeriesWindowCapacity;
	private AtomicInteger forecastConfidence;
	private final AtomicReference<ForecastMethod> forecastMethod = new AtomicReference<ForecastMethod>();
	private AtomicLong deltat;
	private TimeUnit tunit;

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
		this.previousFCPair = new ConcurrentHashMap<String, ForecastMeasurementPair>();
		this.setCurrentConfiguration(configuration, false);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DELTA_TIME, Long.toString(this.deltat.get()));
		configuration.setProperty(CONFIG_PROPERTY_NAME_DELTA_UNIT, this.tunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_FC_METHOD, this.forecastMethod.get().name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_TS_WINDOW_CAPACITY, Integer.toString(this.timeSeriesWindowCapacity.get()));
		configuration.setProperty(CONFIG_PROPERTY_NAME_FC_CONFIDENCE, Integer.toString(this.forecastConfidence.get()));
		return configuration;
	}

	@Override
	public void setCurrentConfiguration(final Configuration config, final boolean update) {
		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_NAME_DELTA_TIME)) {
			this.deltat = new AtomicLong(config.getLongProperty(CONFIG_PROPERTY_NAME_DELTA_TIME));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_NAME_DELTA_UNIT)) {
			this.tunit = TimeUnit.valueOf(config.getStringProperty(CONFIG_PROPERTY_NAME_DELTA_UNIT));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_NAME_FC_METHOD)) {
			this.forecastMethod.set(ForecastMethod.valueOf(config.getStringProperty(CONFIG_PROPERTY_NAME_FC_METHOD)));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_NAME_TS_WINDOW_CAPACITY)) {
			this.timeSeriesWindowCapacity = new AtomicInteger(config.getIntProperty(CONFIG_PROPERTY_NAME_TS_WINDOW_CAPACITY));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_NAME_FC_CONFIDENCE)) {
			this.forecastConfidence = new AtomicInteger(config.getIntProperty(CONFIG_PROPERTY_NAME_FC_CONFIDENCE));
		}
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
					new TimeSeries<Double>(input.getTime(), super.recordsTimeUnitFromProjectContext, this.deltat.get(), this.timeSeriesWindowCapacity.get()));
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

		final ITimeSeries<Double> currentWindow = this.applicationForecastingWindow.get(name);
		currentWindow.append(input.getValue());

		final IForecaster<Double> forecaster = this.forecastMethod.get().getForecaster(currentWindow, this.forecastConfidence.get());

		final IForecastResult result = forecaster.forecast(1);
		super.deliver(OUTPUT_PORT_NAME_FORECAST, result);

		// Check whether we have forecasted points
		if (result.getForecast().getPoints().size() > 0) {

			final double confidenceUpper;
			if (result.getUpper().getValues().size() > 0) {
				confidenceUpper = result.getUpper().getValues().get(0);
			} else {
				confidenceUpper = Double.NaN;
			}

			final double confidenceLower;
			if (result.getLower().getValues().size() > 0) {
				confidenceLower = result.getLower().getValues().get(0);
			} else {
				confidenceLower = Double.NaN;
			}

			final ForecastMeasurementPair previousForecastPair = this.previousFCPair.get(name);

			Double forecast = Double.NaN;
			if (previousForecastPair != null) {
				forecast = previousForecastPair.getForecasted();
			}

			final ForecastMeasurementPair forecastedAndMeasuredResult = new ForecastMeasurementPair(
					name,
					forecast,
					input.getValue(),
					timestamp,
					result.getConfidenceLevel(),
					confidenceUpper,
					confidenceLower,
					result.getMeanAbsoluteScaledError());
			AbstractAnalysisComponent.LOG.info("Forecast: " + forecast + ", Measurement: " + input.getValue() + ", MASE: " + result.getMeanAbsoluteScaledError());
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_MEASURED, forecastedAndMeasuredResult);

			final ForecastMeasurementPair currentForecastPair = new ForecastMeasurementPair(
					name,
					result.getForecast().getPoints().get(0).getValue(),
					input.getValue(),
					timestamp,
					result.getConfidenceLevel(),
					confidenceUpper, confidenceLower,
					result.getMeanAbsoluteScaledError());

			this.previousFCPair.put(name, currentForecastPair);

			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, currentForecastPair);
		} else {
			this.log.error("There are no forecast points to deliver. Perhaps Rserve is not running?");
		}

	}

	/**
	 * Checks if the current application is already known to this filter.
	 *
	 * @param name
	 *            application name
	 */
	private boolean checkInitialization(final String name) {
		return this.applicationForecastingWindow.containsKey(name);
	}
}
