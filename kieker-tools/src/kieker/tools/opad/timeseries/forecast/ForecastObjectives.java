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

package kieker.tools.opad.timeseries.forecast;

/**
 * 
 * @since 1.10
 * @author Tobias Rudolph
 * 
 * 
 *         ForecastObjecives:
 *         maxhorizon
 *         recenthorizon
 *         period
 *         overhead
 *         confidenceLevel
 */
public class ForecastObjectives {

	/**
	 * maximum number of point forecast into the future
	 * 
	 * [1,5) short term
	 * [5,10) medium term
	 * >=10 long term
	 * 
	 * forecast period = timeUnit * horizon
	 * 
	 * The value of maxhorizon setting defines the
	 * maximum number of time series points
	 * to be forecasted.
	 * A recommendation for this setting is the value
	 * of the Frequency setting of the time series,
	 * as a higher horizon setting may lead to broad
	 * confidence intervals.
	 */
	private int maxhorizon = 30;

	/**
	 * The recenthorizon defines the number of time series points
	 * to be forecasted at the beginning and can be dynamically
	 * increased by period factors in the classification setting
	 * up to the MaximumHorizon setting.
	 * This value should be equal or higher than the
	 * ForecastPeriod objective
	 * (if continuous or even overlapping forecasts are needed).
	 */
	private int recenthorizon = 4;

	/**
	 * Forecast period objective defines how often a forecast is
	 * executed in times of new time series points.
	 * For a value of 1 a forecast is requested every new time series point
	 * and can be dynamically increased by period factors in the
	 * classification setting to reach the configured maximum horizon.
	 * This value should be equal or smaller than the StartHorizon
	 * objective (if continuous or even overlapping forecasts are needed)
	 */
	private int period = 2;

	/**
	 * Overhead limits the selection of forecastStrategies according to max overhead
	 * 1 = low - naive, mean:
	 * These two strategies are only applied if less
	 * than InitialSizeThreshold values are in the
	 * time series. The arithmetic mean strategy can
	 * have a forecast accuracy below 1 and therefore
	 * be better than a solely reactive approach
	 * using implicitly the naive strategy.
	 * This is only true in cases of nearly constant
	 * base level of the arrivals rates.
	 * These strategies should be executed as
	 * frequently as possible every new time series point.
	 * 2 = medium - CubicSmoothingSplines, ARIMA101, SimpleExponential Smoothing, Crostons method for intermittent demands:
	 * The strengths of these strategies are the
	 * low computational efforts below 100ms and their
	 * ability to extrapolate the trend component.
	 * They differ in sensitivity to noise level or
	 * seasonal components. These strategies need
	 * to be executed in a high frequency with small horizons.
	 * 3 = high - ExtendedExponential Smoothing, tBATS
	 * The computational effort for both strategies
	 * is below 30 sec for a maximum of 200 time series points.
	 * They differ in the capabilities of modeling seasonal
	 * components.
	 * 4 = very high - ARIMA, tBATS
	 * The computational effort for the ARIMA approach
	 * can reach up to 60 sec for a maximum of
	 * 200 time series points and may achieve smaller
	 * confidence intervals than the tBATS approach.
	 * 
	 * The overhead objective defines the highest overhead group
	 * from which the forecast strategies will be chosen.
	 * A value of 2 may be sufficient if the time series data
	 * have strong trend components that are not overlaid
	 * by seasonal patterns, as the strength of group 2 strategies
	 * is the trend extrapolation.
	 * For time series with seasonal patterns,
	 * a setting of 3 for a maximum forecast
	 * computation time of 30 seconds and 4 for forecast
	 * computation times below 1 minute is recommended.
	 * 
	 */
	private int overhead = 4;

	/**
	 * set the confidence level in percent (value between 0 and 99) for forecasts.
	 */
	private int confidenceLevel = 80;

	/**
	 * @param maxhorizon
	 *            amount max
	 * @param recenthorizon
	 *            recent length of the timeseries
	 * @param period
	 *            period
	 * @param overhead
	 *            overhead of forecast
	 * @param confidence
	 *            confidence level
	 */

	public ForecastObjectives(final int maxhorizon, final int recenthorizon, final int period, final int overhead, final int confidence) {
		this.maxhorizon = maxhorizon;
		this.recenthorizon = recenthorizon;
		this.period = period;
		this.overhead = overhead;
		this.confidenceLevel = confidence;
	}

	public int getMaxHorizon() {
		return this.maxhorizon;
	}

	public void setMaxHorizon(final int parametermaxhorizon) {
		this.maxhorizon = parametermaxhorizon;
	}

	public int getRecentHorizon() {
		return this.recenthorizon;
	}

	public void setRecentHorizon(final int parameterrecenthorizon) {
		this.recenthorizon = parameterrecenthorizon;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(final int period) {
		this.period = period;
	}

	public int getOverhead() {
		return this.overhead;
	}

	public void setOverhead(final int overhead) {
		this.overhead = overhead;
	}

	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}

	public void setConfidenceLevel(final int confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}
}
