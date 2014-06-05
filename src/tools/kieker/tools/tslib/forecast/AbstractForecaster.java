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
import kieker.tools.tslib.TimeSeries;

/**
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld
 * 
 * @since 1.9
 * 
 * @param <T>
 *            The type of the forecaster.
 */
public abstract class AbstractForecaster<T> implements IForecaster<T> {

	private final ITimeSeries<T> historyTimeseries;
	private final int confidenceLevel;

	public AbstractForecaster(final ITimeSeries<T> historyTimeseries) {
		this(historyTimeseries, 0);
	}

	public AbstractForecaster(final ITimeSeries<T> historyTimeseries, final int confidenceLevel) {
		this.historyTimeseries = historyTimeseries;
		this.confidenceLevel = confidenceLevel;
	}

	@Override
	public ITimeSeries<T> getTsOriginal() {
		return this.historyTimeseries;
	}

	protected ITimeSeries<T> prepareForecastTS() {
		final ITimeSeries<T> history = this.getTsOriginal();

		// The starting point of the FC series is calculated by _one_ additional
		// tick...
		// ... plus the end point of the historic series
		final long startTime = history.getEndTime();
		final TimeSeries<T> tsFC = new TimeSeries<T>(startTime,
				history.getDeltaTime(), history.getDeltaTimeUnit());

		return tsFC;
	}

	@Override
	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}
}
