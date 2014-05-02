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

package kieker.tools.tslib.forecast;

import java.util.List;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.wcf.wibClassification.ClassificationUtility;

/**
 * Result of a time series forecast, e.g., computed by {@link IForecaster}. If additional fields are required, {@link IForecaster}s should extend this class.
 * 
 * @author Andre van Hoorn
 * 
 */
public class ForecastResult implements IForecastResult {

	private final ITimeSeries<Double> tsForecast;
	private final ITimeSeries<Double> tsOriginal;

	private final int confidenceLevel;
	private final double meanAbsoluteScaledError;
	private final ForecastMethod fcStrategy;

	private final ITimeSeries<Double> tsUpper;
	private final ITimeSeries<Double> tsLower;

	/**
	 * 
	 * @param tsForecast
	 *            TimesSeries
	 * @param tsOriginal
	 *            TimeSeries
	 * @param tsconfidenceLevel
	 *            confidentLevel
	 * @param tsmeanAbsoluteScaledError
	 *            MASE
	 * @param tsLower
	 *            ??
	 * @param tsUpper
	 *            ??
	 * @param fcStrategy
	 *            FC Method
	 */
	public ForecastResult(final ITimeSeries<Double> tsForecast, final ITimeSeries<Double> tsOriginal, final int tsconfidenceLevel,
			final double tsmeanAbsoluteScaledError, final ITimeSeries<Double> tsLower,
			final ITimeSeries<Double> tsUpper, final ForecastMethod fcStrategy) {
		this.tsForecast = tsForecast;
		this.tsOriginal = tsOriginal;
		this.meanAbsoluteScaledError = tsmeanAbsoluteScaledError;

		this.confidenceLevel = tsconfidenceLevel;
		this.tsUpper = tsUpper;
		this.tsLower = tsLower;
		this.fcStrategy = fcStrategy;

	}

	/**
	 * Constructs a {@link ForecastResult} with confidence level <code>0</code>, where the time series returned {@link #getLower()} by {@link #getUpper()} are the
	 * forecast series.
	 * 
	 * @param tsForecast
	 *            Timeseries with forecast
	 * @param tsOriginal
	 *            Timeseries with orginal
	 * @param fcStrategy
	 *            forecastMethod
	 * 
	 */
	public ForecastResult(final ITimeSeries<Double> tsForecast, final ITimeSeries<Double> tsOriginal, final ForecastMethod fcStrategy) {
		this(tsForecast, tsOriginal, 0, 0, tsForecast, tsForecast, fcStrategy); // tsForecast also lower/upper
	}

	
	 
	  // TODO Auto-generated constructor stub
	

	public ITimeSeries<Double> getForecast() {
		return this.tsForecast;
	}

	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}

	public ITimeSeries<Double> getUpper() {
		return this.tsUpper;
	}

	public ITimeSeries<Double> getLower() {
		return this.tsLower;
	}

	public ITimeSeries<Double> getOriginal() {
		return this.tsOriginal;
	}

	@Override
	public String toString() {
		final StringBuilder strB = new StringBuilder("\n");
		strB.append("tsForecast: ").append(this.tsForecast.toString()).append("\n");
		strB.append("tsOriginal: ").append(this.tsOriginal.toString()).append("\n");
		strB.append("confidenceLevel: ").append(this.confidenceLevel).append("\n");
		strB.append("tsUpper: ").append(this.tsUpper).append("\n");
		strB.append("tsLower: ").append(this.tsLower).append("\n");
		return strB.toString();
	}

	@Override
	public double getMeanAbsoluteScaledError() {
		return this.meanAbsoluteScaledError;
	}

	public ForecastMethod getFcStrategy() {
		return this.fcStrategy;
	}

	@Override
	public boolean isPlausible() {
		if ((this.meanAbsoluteScaledError == 0) || Double.isNaN(this.meanAbsoluteScaledError)) {
			return false;
		}
		final double maximumObserved = ClassificationUtility.calcMaximum(this.tsOriginal);
		final List<Double> values = this.tsForecast.getValues();
		for (final Double value : values) {
			if ((value > (maximumObserved * 2)) || (value < 0)) {
				return false;
			}
		}
		return true;
	}
}
