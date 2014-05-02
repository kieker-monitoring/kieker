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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.mongodb.DBCollection;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.tools.configuration.AbstractUpdateableFilterPlugin;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.IForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.IForecaster;
import kieker.tools.tslib.forecast.historicdata.MongoDBConnection;
import kieker.tools.tslib.forecast.historicdata.PatternCheckingForecaster;
import kieker.tools.tslib.forecast.wcf.wibManagement.WorkloadIntensityBehavior;

/**
 * 
 * Computes a forecast for every incoming measurement from different
 * applications. If an collective anomaly is assumed, an alternative forecasting
 * approach (pattern checking forecaster) is used. The Forecasting Filter is choosed automaticly by the wcf selftunig alog
 * 
 * @author Andreas Eberlein, Tobias Rudolph, Tom Frotscher
 * 
 */

@Plugin(name = "Extended Selftuning Forecasting Filter", outputPorts = {
	@OutputPort(eventTypes = { IForecastResult.class }, name = ExtendedSelfTuningForecastingFilter.OUTPUT_PORT_NAME_FORECAST),
	@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = ExtendedSelfTuningForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT) },
		configuration = {
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, defaultValue = "1000"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT, defaultValue = "MILLISECONDS"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_FC_METHOD, defaultValue = "MEAN", updateable = true),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_TS_WINDOW_CAPACITY, defaultValue = "60"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_ANOMALY_THRESHOLD, defaultValue = "0.5", updateable = true),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_CONSECUTIVE_ANOMALY_THRESHOLD, defaultValue = "5", updateable = true),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_PATTERN_LENGTH, defaultValue = "24"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_PATTERN_UNIT, defaultValue = "HOURS"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_PATTERN_WINDOW_UNIT, defaultValue = "MINUTES"),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_ON, defaultValue = "true", updateable = true),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_CLASSPERIOD, defaultValue = "1", updateable = false),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_CONFIDENCE, defaultValue = "80", updateable = false),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_STARTHORIZON, defaultValue = "1", updateable = false),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_FCPERIOD, defaultValue = "1", updateable = false),
			@Property(name = ExtendedSelfTuningForecastingFilter.CONFIG_PROPERTY_SELFTUNING_OVERHEAD, defaultValue = "3", updateable = false)
		})
public class ExtendedSelfTuningForecastingFilter extends AbstractUpdateableFilterPlugin {

	/** Name of the input port receiving the measurements. */
	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";
	/** Name of the output port delivering the forecasts. */
	public static final String OUTPUT_PORT_NAME_FORECAST = "forecast";
	/** Name of the output port delivering the measurement-forecast-pairs. */
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";
	/** Name of the property determining the deltatime. */
	public static final String CONFIG_PROPERTY_DELTA_TIME = "deltatime";
	/** Name of the property determining the timeunit of the deltatime. */
	public static final String CONFIG_PROPERTY_DELTA_UNIT = "deltaunit";
	/** Name of the property determining the forecasting method. */
	public static final String CONFIG_PROPERTY_FC_METHOD = "fcmethod";
	/** Name of the property determining the capacity of the timeseries window. Take value <= 0 for infinite buffer size. */
	public static final String CONFIG_PROPERTY_TS_WINDOW_CAPACITY = "tswcapacity";
	/** Name of the property determining the threshold for anomalies. */
	public static final String CONFIG_PROPERTY_ANOMALY_THRESHOLD = "anomalyth";
	/** Name of the property determining the threshold for consecutive anomalies. */
	public static final String CONFIG_PROPERTY_CONSECUTIVE_ANOMALY_THRESHOLD = "consanomalyth";
	/** Name of the property determing if selftuning is used or not. */
	public static final String CONFIG_PROPERTY_SELFTUNING_ON = "selftuning";
	/** Name of the property defining how often a classification is executed in times of new time series points. */
	public static final String CONFIG_PROPERTY_SELFTUNING_CLASSPERIOD = "classperiod";
	/** Name of the property defining the confidence level α of the returned forecast confidence intervals. */
	public static final String CONFIG_PROPERTY_SELFTUNING_CONFIDENCE = "confidence";
	/** Name of the property defining the number of time series points to be forecasted at the beginning. */
	public static final String CONFIG_PROPERTY_SELFTUNING_STARTHORIZON = "startHorizon";
	/** Name of the property defining how often a forecast is executed in times of new time series points. */
	public static final String CONFIG_PROPERTY_SELFTUNING_FCPERIOD = "fcperiod";
	/** Name of the property defining the highest overhead group from which the forecast strategies will be chosen. */
	public static final String CONFIG_PROPERTY_SELFTUNING_OVERHEAD = "overhead";
	/** Name of the property determining the length of a seasonal pattern. */
	public static final String CONFIG_PROPERTY_PATTERN_LENGTH = "patternlength";
	/** Name of the property determining the time unit of the pattern length. */
	public static final String CONFIG_PROPERTY_PATTERN_UNIT = "patternunit";
	/** Name of the property determining the time unit of the pattern length. */
	public static final String CONFIG_PROPERTY_PATTERN_WINDOW_UNIT = "patterwindownunit";

	private final ConcurrentHashMap<String, ITimeSeries<Double>> applicationForecastingWindow;
	private final ConcurrentHashMap<String, Integer> consecutiveAnomalyCount;
	private final ConcurrentHashMap<String, WorkloadIntensityBehavior> wibStore;

	private AtomicLong deltat;
	private AtomicReference<TimeUnit> tunit;
	private AtomicReference<ForecastMethod> forecastMethod;
	private AtomicInteger timeSeriesWindowCapacity;
	private AtomicReference<Double> anomalyThreshold;
	private AtomicBoolean selftuningbool;
	private AtomicInteger classperiod;
	private AtomicInteger confidence;
	private AtomicInteger startHorizon;
	private AtomicInteger fcperiod;
	private AtomicInteger overhead;

	private IForecaster<Double> forecaster;

	private AtomicInteger consecutiveAnomalyThreshold;
	private AtomicLong patternLength;
	private AtomicReference<TimeUnit> patternTimeunit;
	private AtomicReference<TimeUnit> patternWindowLengthUnit;
	private final DBCollection coll;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param configuration
	 *            Configuration of this component
	 * @param projectContext
	 *            ProjectContext of this component
	 */
	public ExtendedSelfTuningForecastingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.setCurrentConfiguration(configuration, false);

		this.applicationForecastingWindow = new ConcurrentHashMap<String, ITimeSeries<Double>>();
		this.consecutiveAnomalyCount = new ConcurrentHashMap<String, Integer>();
		this.wibStore = new ConcurrentHashMap<String, WorkloadIntensityBehavior>();

		final MongoDBConnection con = new MongoDBConnection();
		this.coll = con.getColl();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_DELTA_TIME, Long.toString(this.deltat.get()));
		configuration.setProperty(CONFIG_PROPERTY_DELTA_UNIT, this.tunit.get().name());
		configuration.setProperty(CONFIG_PROPERTY_FC_METHOD, this.forecastMethod.get().name());
		configuration.setProperty(CONFIG_PROPERTY_TS_WINDOW_CAPACITY, Integer.toString(this.timeSeriesWindowCapacity.get()));
		configuration.setProperty(CONFIG_PROPERTY_PATTERN_LENGTH, Long.toString(this.patternLength.get()));
		configuration.setProperty(CONFIG_PROPERTY_PATTERN_UNIT, this.patternTimeunit.get().name());
		configuration.setProperty(CONFIG_PROPERTY_PATTERN_WINDOW_UNIT, this.patternWindowLengthUnit.get().name());
		configuration.setProperty(CONFIG_PROPERTY_CONSECUTIVE_ANOMALY_THRESHOLD, Integer.toString(this.consecutiveAnomalyThreshold.get()));
		configuration.setProperty(CONFIG_PROPERTY_ANOMALY_THRESHOLD, Double.toString(this.anomalyThreshold.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_ON, Boolean.toString(this.selftuningbool.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_CLASSPERIOD, Integer.toString(this.classperiod.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_CONFIDENCE, Integer.toString(this.confidence.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_STARTHORIZON, Integer.toString(this.startHorizon.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_FCPERIOD, Integer.toString(this.fcperiod.get()));
		configuration.setProperty(CONFIG_PROPERTY_SELFTUNING_OVERHEAD, Integer.toString(this.overhead.get()));
		return configuration;
	}

	/**
	 * Represents the input port for measurements.
	 * 
	 * @param input
	 *            Incoming measurements
	 */
	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = ExtendedSelfTuningForecastingFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputEvent(final NamedDoubleTimeSeriesPoint input) {
		if (this.checkInitialization(input.getName())) {
			this.processInput(input, input.getTime(), input.getName(), this.consecutiveAnomalyCount.get(input.getName()));
		} else {
			this.applicationForecastingWindow.put(input.getName(),
					new TimeSeries<Double>(System.currentTimeMillis(), this.deltat.get(), this.tunit.get(), this.timeSeriesWindowCapacity.get()));
			this.consecutiveAnomalyCount.put(input.getName(), 0);
			if (this.selftuningbool.get()) {
				final int maxHorizon = this.timeSeriesWindowCapacity.get();
				final WorkloadIntensityBehavior newWib = new WorkloadIntensityBehavior(this.classperiod.get(), this.confidence.get(), maxHorizon,
						this.startHorizon.get(), this.fcperiod.get(), this.overhead.get());
				this.wibStore.put(input.getName(), newWib);
			}
			this.processInput(input, input.getTime(), input.getName(), this.consecutiveAnomalyCount.get(input.getName()));
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
	 * @param cons
	 *            Count of consecutive anomalies
	 */
	public void processInput(final NamedDoubleTimeSeriesPoint input,
			final long timestamp, final String name, final int cons) {
		final ITimeSeries<Double> actualWindow = this.applicationForecastingWindow.get(name);
		final WorkloadIntensityBehavior actualWib = this.wibStore.get(name);
		final PatternCheckingForecaster histForecaster = new PatternCheckingForecaster(
				actualWindow, this.patternLength.get(), this.patternTimeunit.get(), this.patternWindowLengthUnit.get(), this.coll,
				this.timeSeriesWindowCapacity.get(), input);
		if ((cons > this.consecutiveAnomalyThreshold.get())
				&& histForecaster.getForecastWindowFromDB()) {
			// Pattern checking forecaster selected
			final int forecastlength = 1;

			final IForecastResult result = histForecaster.forecast(forecastlength);
			// Clear Sliding Window because else it is filled with abnormal values
			this.applicationForecastingWindow.put(name,
					new TimeSeries<Double>(System.currentTimeMillis(), this.deltat.get(), this.tunit.get(), this.timeSeriesWindowCapacity.get()));
			this.adjustConsecutiveCount(result.getForecast().getPoints().get(0).getValue(), input);

			final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
					name, result.getForecast().getPoints().get(0).getValue(), input.getValue(), timestamp);
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
		} else {
			if (!input.getValue().isNaN()) {
				actualWindow.append(input.getValue());
				if (this.selftuningbool.get()) {
					// Use WIB method
					actualWib.setTimeSeries(actualWindow);
					actualWib.callForecaster();
					final IForecastResult result = actualWib.getFcResult();
					final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
							name, result.getForecast().getPoints().get(0).getValue(), input.getValue(), timestamp);
					super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
				} else {
					// Use selected specific forecaster
					this.forecaster = this.forecastMethod.get().getForecaster(actualWindow);
					final IForecastResult result = this.forecaster.forecast(1);
					this.adjustConsecutiveCount(result.getForecast().getPoints().get(0).getValue(), input);

					final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
							name, result.getForecast().getPoints().get(0).getValue(), input.getValue(), timestamp);
					super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
				}
			} else {
				final ForecastMeasurementPair fmp = new ForecastMeasurementPair(name, Double.NaN, Double.NaN, timestamp);
				super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
			}
		}
	}

	/**
	 * Adjusts the consecutive anomaly count. If additional anomaly is found,
	 * then the count is increased, if normal value is found, the count is set
	 * to 0.
	 * 
	 * @param altValue
	 *            alternative forecasting value from the pattern checking
	 *            forecaster
	 * @param input
	 *            actual input value
	 */
	private void adjustConsecutiveCount(final double altValue, final NamedDoubleTimeSeriesPoint input) {
		final double anomalyScore = this.calculateAnomalyScore(altValue, input.getDoubleValue());
		final boolean anomaly = this.detectAnomaly(anomalyScore);
		if (anomaly) {
			final int consecutive = this.consecutiveAnomalyCount.get(input.getName());
			this.consecutiveAnomalyCount.put(input.getName(), consecutive + 1);
		} else {
			final int value = 0;
			this.consecutiveAnomalyCount.put(input.getName(), value);
		}
	}

	private boolean detectAnomaly(final double anomalyScore) {
		return anomalyScore >= this.anomalyThreshold.get();
	}

	/**
	 * Calculates the anomaly score, based on the actual values and the long
	 * term data extracted from the database.
	 * 
	 * @param altValue
	 *            Alternative reference value
	 * @param dr
	 *            Actual analysis results
	 * @return Alternative anomaly score
	 */
	private double calculateAnomalyScore(final double altValue, final double value) {
		Double alternativeScore = null;

		final double nextpredicted = altValue;
		final double measuredValue = value;

		final double difference = nextpredicted - measuredValue;
		final double sum = nextpredicted + measuredValue;
		alternativeScore = Math.abs(difference / sum);

		return alternativeScore;
	}

	/**
	 * Checks if the current application is already known to this filter.
	 * 
	 * @param name
	 *            Application name
	 */
	private boolean checkInitialization(final String name) {
		return this.applicationForecastingWindow.containsKey(name) && this.wibStore.containsKey(name);
	}

	@Override
	public void setCurrentConfiguration(final Configuration config, final boolean update) {
		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_DELTA_TIME)) {
			this.deltat = new AtomicLong(config.getLongProperty(CONFIG_PROPERTY_DELTA_TIME));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_DELTA_UNIT)) {
			this.tunit = new AtomicReference<TimeUnit>(TimeUnit.valueOf(config.getStringProperty(CONFIG_PROPERTY_DELTA_UNIT)));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_FC_METHOD)) {
			this.forecastMethod = new AtomicReference<ForecastMethod>(
					ForecastMethod.valueOf(this.configuration.getStringProperty(CONFIG_PROPERTY_FC_METHOD)));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_TS_WINDOW_CAPACITY)) {
			this.timeSeriesWindowCapacity = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_TS_WINDOW_CAPACITY));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_PATTERN_LENGTH)) {
			this.patternLength = new AtomicLong(this.configuration.getLongProperty(CONFIG_PROPERTY_PATTERN_LENGTH));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_PATTERN_UNIT)) {
			this.patternTimeunit = new AtomicReference<TimeUnit>(TimeUnit.valueOf(this.configuration.getStringProperty(CONFIG_PROPERTY_PATTERN_UNIT)));

		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_PATTERN_WINDOW_UNIT)) {
			this.patternWindowLengthUnit = new AtomicReference<TimeUnit>(
					TimeUnit.valueOf(this.configuration.getStringProperty(CONFIG_PROPERTY_PATTERN_WINDOW_UNIT)));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_CONSECUTIVE_ANOMALY_THRESHOLD)) {
			this.consecutiveAnomalyThreshold = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_CONSECUTIVE_ANOMALY_THRESHOLD));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_ANOMALY_THRESHOLD)) {
			final String thresholdString = this.configuration.getStringProperty(CONFIG_PROPERTY_ANOMALY_THRESHOLD);
			this.anomalyThreshold = new AtomicReference<Double>(Double.parseDouble(thresholdString));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_ON)) {
			this.selftuningbool = new AtomicBoolean(this.configuration.getBooleanProperty(CONFIG_PROPERTY_SELFTUNING_ON));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_CLASSPERIOD)) {
			this.classperiod = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_SELFTUNING_CLASSPERIOD));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_CONFIDENCE)) {
			this.confidence = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_SELFTUNING_CONFIDENCE));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_STARTHORIZON)) {
			this.startHorizon = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_SELFTUNING_STARTHORIZON));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_FCPERIOD)) {
			this.fcperiod = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_SELFTUNING_FCPERIOD));
		}

		if (!update || this.isPropertyUpdateable(CONFIG_PROPERTY_SELFTUNING_OVERHEAD)) {
			this.overhead = new AtomicInteger(this.configuration.getIntProperty(CONFIG_PROPERTY_SELFTUNING_OVERHEAD));
		}
	}
}
