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

package kieker.tools.opad.timeseries;

import kieker.tools.opad.timeseries.anomalycalculators.IAnomalyScoreCalculator;
import kieker.tools.opad.timeseries.anomalycalculators.SimpleAnomalyScoreCalculator;
import kieker.tools.opad.timeseries.forecast.IForecaster;
import kieker.tools.opad.timeseries.forecast.arima.ARIMA101Forecaster;
import kieker.tools.opad.timeseries.forecast.arima.ARIMAForecaster;
import kieker.tools.opad.timeseries.forecast.croston.CrostonForecaster;
import kieker.tools.opad.timeseries.forecast.cs.CSForecaster;
import kieker.tools.opad.timeseries.forecast.ets.ETSForecaster;
import kieker.tools.opad.timeseries.forecast.mean.MeanForecaster;
import kieker.tools.opad.timeseries.forecast.mean.MeanForecasterJava;
import kieker.tools.opad.timeseries.forecast.naive.NaiveForecaster;
import kieker.tools.opad.timeseries.forecast.ses.SESRForecaster;

/**
 *
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld, Tobias Rudolph, Andreas Eberlein
 * @since 1.10
 */
public enum ForecastMethod {

	/** {@link ARIMAForecaster} */
	ARIMA,

	/** {@link ARIMA101Forecaster} */
	ARIMA101,

	/** {@link CrostonForecaster} */
	CROSTON,

	/** {@link CSForecaster} */
	CS,

	/** {@link ETSForecaster} */
	ETS,

	/** {@link MeanForecaster} */
	MEAN,

	/** {@link MeanForecasterJava} */
	MEANJAVA,

	/** {@link NaiveForecaster} */
	NAIVE,

	/** {@link SESRForecaster} */
	SES;

	/**
	 *
	 * @param history
	 *            Timeseries which will be forecasted
	 *
	 * @return the forecaster for this algorithm
	 *
	 * @throws UnsupportedOperationException
	 *             if forecaster not instantiable or not defined.
	 */
	public IForecaster<Double> getForecaster(final ITimeSeries<Double> history) { 
		switch (this) {
		case ARIMA:
			return new ARIMAForecaster(history);
		case ARIMA101:
			return new ARIMA101Forecaster(history);
		case CROSTON:
			return new CrostonForecaster(history);
		case CS:
			return new CSForecaster(history);
		case ETS:
			return new ETSForecaster(history);
		case MEAN:
			return new MeanForecaster(history);
		case MEANJAVA:
			return new MeanForecasterJava(history);
		case NAIVE:
			return new NaiveForecaster(history);
		case SES:
			return new SESRForecaster(history);
		default:
			throw new UnsupportedOperationException("No forecaster defined for " + this.toString());
		}
	}

	/**
	 *
	 * @param history
	 *            Timeseries which will be forecasted
	 * @param alpha
	 *
	 *            confidence level
	 * @return the forecaster for this algorithm
	 *
	 * @throws IllegalArgumentException
	 *             if forecaster not instantiable or not defined.
	 */
	public IForecaster<Double> getForecaster(final ITimeSeries<Double> history, final int alpha) {
		switch (this) {
		case ARIMA:
			return new ARIMAForecaster(history, alpha);
		case ARIMA101:
			return new ARIMA101Forecaster(history, alpha);
		case CROSTON:
			return new CrostonForecaster(history, alpha);
		case CS:
			return new CSForecaster(ForecastMethod.getLastXofTS(history, 30), alpha);
		case ETS:
			return new ETSForecaster(history, alpha);
		case MEAN:
			return new MeanForecaster(ForecastMethod.getLastXofTS(history, 10), alpha);
		case MEANJAVA:
			return new MeanForecasterJava(ForecastMethod.getLastXofTS(history, 10)); // confidence level? #1346
		case NAIVE:
			return new NaiveForecaster(history, alpha);
		case SES:
			return new SESRForecaster(history, alpha);
		default:
			throw new UnsupportedOperationException("No forecaster defined for " + this.toString());
		}
	}

	public IAnomalyScoreCalculator<Double> getAnomalyScoreCalculator() {
		return new SimpleAnomalyScoreCalculator();
	}

	// Was extracted from ClassificationUtility in WCF/TBATS as it is not yet integrated:
	/**
	 * Returns a new time series object shortened to the last x values.
	 *
	 * @param ts
	 *            timeseries
	 * @param x
	 *            last x value
	 * @return new time series object
	 */
	private static ITimeSeries<Double> getLastXofTS(final ITimeSeries<Double> ts, final int x) {
		if (ts.size() >= x) {
			Double[] a = new Double[ts.size()];
			a = ts.getValues().toArray(a);
			final Double[] b = new Double[x];
			for (int i = 0; i < x; i++) {
				b[i] = a[(ts.size() - x) + i];
			}
			long newStartTime = ts.getStartTime();
			newStartTime += (ts.size() - x) * ts.getDeltaTimeUnit().toMillis(ts.getDeltaTime());
			final ITimeSeries<Double> tsLastX = new TimeSeries<>(newStartTime, ts.getTimeSeriesTimeUnit(), ts.getDeltaTime(), ts.getDeltaTimeUnit(),
					ts.getCapacity());
			tsLastX.appendAll(b);
			return tsLastX;
		} else {
			return ts;
		}
	}
}
