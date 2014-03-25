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

package kieker.tools.tslib.forecast;

import kieker.tools.tslib.ITimeSeries;

/**
 * Result of a time series forecast, e.g., computed by {@link IForecaster}. If additional fields are required, {@link IForecaster}s should extend this class.
 * 
 * @author Andre van Hoorn
 * @since 1.9
 * 
 */
public class ForecastResult<T> implements IForecastResult<T> {

	private final ITimeSeries<T> tsForecast;
	private final ITimeSeries<T> tsOriginal;

	private final int confidenceLevel;
	private final ITimeSeries<T> tsUpper;
	private final ITimeSeries<T> tsLower;

	public ForecastResult(final ITimeSeries<T> tsForecast, final ITimeSeries<T> tsOriginal, final int confidenceLevel, final ITimeSeries<T> tsLower,
			final ITimeSeries<T> tsUpper) {
		this.tsForecast = tsForecast;
		this.tsOriginal = tsOriginal;

		this.confidenceLevel = confidenceLevel;
		this.tsUpper = tsUpper;
		this.tsLower = tsLower;
	}

	/**
	 * Constructs a {@link ForecastResult} with confidence level <code>0</code>, where the time series returned {@link #getLower()} by {@link #getUpper()} are the
	 * forecast series.
	 * 
	 * @param tsForecast
	 */
	public ForecastResult(final ITimeSeries<T> tsForecast, final ITimeSeries<T> tsOriginal) {
		this(tsForecast, tsOriginal, 0, tsForecast, tsForecast); // tsForecast also lower/upper
	}

	public ITimeSeries<T> getForecast() {
		return this.tsForecast;
	}

	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}

	public ITimeSeries<T> getUpper() {
		return this.tsUpper;
	}

	public ITimeSeries<T> getLower() {
		return this.tsLower;
	}

	public ITimeSeries<T> getOriginal() {
		return this.tsOriginal;
	}

	@Override
	public String toString() {
		final String lineSeperator = System.getProperty("line.separatorr");
		final StringBuilder strB = new StringBuilder(59); // There are at least 59 characters in the following appening

		strB.append(lineSeperator);
		strB.append("tsForecast: ").append(this.tsForecast.toString()).append(lineSeperator);
		strB.append("tsOriginal: ").append(this.tsOriginal.toString()).append(lineSeperator);
		strB.append("confidenceLevel: ").append(this.confidenceLevel).append(lineSeperator);
		strB.append("tsUpper: ").append(this.tsUpper).append(lineSeperator);
		strB.append("tsLower: ").append(this.tsLower).append(lineSeperator);

		return strB.toString();
	}
}
