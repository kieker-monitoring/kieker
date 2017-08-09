/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast;

import java.util.List;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;

/**
 * Result of a time series forecast, e.g., computed by {@link IForecaster}. If additional fields are required, {@link IForecaster}s should extend this class.
 * 
 * @since 1.10
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

	@Override
	public ITimeSeries<Double> getForecast() {
		return this.tsForecast;
	}

	@Override
	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}

	@Override
	public ITimeSeries<Double> getUpper() {
		return this.tsUpper;
	}

	@Override
	public ITimeSeries<Double> getLower() {
		return this.tsLower;
	}

	@Override
	public ITimeSeries<Double> getOriginal() {
		return this.tsOriginal;
	}

	@Override
	public String toString() {
		final StringBuilder strB = new StringBuilder(71);
		strB.append('\n' + "tsForecast: ");
		strB.append(this.tsForecast.toString());
		strB.append('\n' + "tsOriginal: ");
		strB.append(this.tsOriginal.toString());
		strB.append('\n' + "confidenceLevel: ");
		strB.append(this.confidenceLevel);
		strB.append('\n' + "tsUpper: ");
		strB.append(this.tsUpper);
		strB.append('\n' + "tsLower: ");
		strB.append(this.tsLower);
		strB.append('\n');
		return strB.toString();
	}

	@Override
	public double getMeanAbsoluteScaledError() {
		return this.meanAbsoluteScaledError;
	}

	@Override
	public ForecastMethod getFcStrategy() {
		return this.fcStrategy;
	}

	/**
	 * Checks whether the input seems to be plausible.
	 * 
	 * @return true if plausible, else false
	 */
	@Override
	public boolean isPlausible() {
		if ((this.meanAbsoluteScaledError == 0) || Double.isNaN(this.meanAbsoluteScaledError)) {
			return false;
		}
		final double maximumObserved = ForecastResult.calcMaximum(this.tsOriginal);
		final List<Double> values = this.tsForecast.getValues();
		for (final Double value : values) {
			if ((value > (maximumObserved * 2)) || (value < 0)) {
				return false;
			}
		}
		return true;
	}

	// Was extracted from ClassificationUtility in WCF/TBATS as it is not yet integrated:
	/**
	 * @param ts
	 *            timeseries
	 * @return maximum value of the time series
	 */
	private static double calcMaximum(final ITimeSeries<Double> ts) {
		final List<Double> values = ts.getValues();
		double max = 0;
		for (final double t : values) {
			if (t > max) {
				max = t;
			}
		}
		return max;
	}
}
