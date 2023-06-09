/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.TimeSeries;

/**
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld
 * @since 1.10
 *
 * @param <T>
 *            The type of the forecaster.
 */
public abstract class AbstractForecaster<T> implements IForecaster<T> {

	/**
	 * Get Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractForecaster.class);

	private static final boolean CONFIDENCE_SUPPORTED_BY_DEFAULT = true;

	private final ITimeSeries<T> historyTimeseries;
	private final int confidenceLevel;

	private boolean warningAlreadyLogged;

	public AbstractForecaster(final ITimeSeries<T> historyTimeseries) {
		this(historyTimeseries, 0);
	}

	/**
	 *
	 * @param historyTimeseries
	 *            TS
	 * @param confidenceLevel
	 *            value for confidencelevel
	 */
	public AbstractForecaster(final ITimeSeries<T> historyTimeseries, final int confidenceLevel) {
		this.historyTimeseries = historyTimeseries;
		this.confidenceLevel = confidenceLevel;
	}

	@Override
	public ITimeSeries<T> getTsOriginal() {
		return this.historyTimeseries;
	}

	/**
	 *
	 * @return TS
	 */
	protected ITimeSeries<T> prepareForecastTS() {
		final ITimeSeries<T> history = this.getTsOriginal();

		final long startTime = history.getStartTime();
		return new TimeSeries<T>(startTime, history.getTimeSeriesTimeUnit(),
				history.getDeltaTime(), history.getDeltaTimeUnit());
	}

	protected boolean supportsConfidence() {
		return CONFIDENCE_SUPPORTED_BY_DEFAULT;
	}

	@Override
	public int getConfidenceLevel() {
		if (this.supportsConfidence()) {
			return this.confidenceLevel;
		} else {
			if (!this.warningAlreadyLogged) {
				LOGGER.warn("Confidence level not supported. Falling back to 0.0.");
				this.warningAlreadyLogged = true;
			}
			return 0;
		}
	}
}
