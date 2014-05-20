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

package kieker.tools.tslib;

import kieker.tools.tslib.anomalycalculators.IAnomalyCalculator;
import kieker.tools.tslib.anomalycalculators.SimpleAnomalyCalculator;
import kieker.tools.tslib.forecast.IForecaster;
import kieker.tools.tslib.forecast.arima.ARIMA101Forecaster;
import kieker.tools.tslib.forecast.arima.ARIMAForecaster;
import kieker.tools.tslib.forecast.croston.CrostonForecaster;
import kieker.tools.tslib.forecast.cs.CSForecaster;
import kieker.tools.tslib.forecast.ets.ETSForecaster;
import kieker.tools.tslib.forecast.mean.MeanForecaster;
import kieker.tools.tslib.forecast.mean.MeanForecasterJava;
import kieker.tools.tslib.forecast.naive.NaiveForecaster;
import kieker.tools.tslib.forecast.ses.SESRForecaster;
import kieker.tools.tslib.forecast.windowstart.WindowStartForecaster;

/**
 * 
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld, Tobias Rudolph, Andreas Eberlein
 * @since 1.10
 * 
 */
public enum ForecastMethod {

	/**
	 * different FC algos
	 */
	MEAN, MEANJAVA, SES, ETS, ARIMA101, WINDOWSTART, ARIMA, CROSTON, NAIVE, TBATS, CS, PCF, WSF, INACT;

	/**
	 * 
	 * @param history
	 *            Timeseries which will be forecasted
	 * @return FC
	 */
	public IForecaster<Double> getForecaster(final ITimeSeries<Double> history) {
		switch (this) {
		// Don't use this forecast, the implementation of the SESR Forecast looks disgusting and without the R_script the forecast doesn't work at all,
		// use for example the SESForecast instead
		// case SESR:
		// return new SESRForecaster(history);
		case SES:
			return new SESRForecaster(history);
		case ETS:
			return new ETSForecaster(history);
		case MEANJAVA:
			return new MeanForecasterJava(history);
		case MEAN:
			return new MeanForecaster(history);
		case ARIMA101:
			return new ARIMA101Forecaster(history);
		case CROSTON:
			return new CrostonForecaster(history);
		case NAIVE:
			return new NaiveForecaster(history);
		case CS:
			return new CSForecaster(history);

			// WCF/TBATS are not yet integrated
			// case TBATS:
			// return new TBATSForecaster(history);

		case ARIMA:
			return new ARIMAForecaster(history);
		case WINDOWSTART:
			return new WindowStartForecaster(history);
		case INACT:
			return null;
		default:
			return new MeanForecaster(history);
		}
	}

	/**
	 * 
	 * @param ts
	 *            Timeseries
	 * @param alpha
	 *            confidentlevel
	 * @return Forecaster
	 */
	public IForecaster<Double> getForecaster(final ITimeSeries<Double> ts, final int alpha) {
		switch (this) {
		case NAIVE:
			return new NaiveForecaster(ts, alpha);
		case MEANJAVA:
			return new MeanForecasterJava(ForecastMethod.getLastXofTS(ts, 10));
		case MEAN:
			return new MeanForecaster(ForecastMethod.getLastXofTS(ts, 10), alpha);
		case CS: // Cubic Smoothing Splines
			return new CSForecaster(ForecastMethod.getLastXofTS(ts, 30), alpha);
		case SES: // Simple Exponential Smoothing
			return new SESRForecaster(ts, alpha);
		case CROSTON: // Croston's Method
			return new CrostonForecaster(ts);
		case ETS: // Extended Exponential Smoothing
			return new ETSForecaster(ts, alpha);
		case ARIMA101:
			return new ARIMA101Forecaster(ts, alpha);
		case ARIMA:
			return new ARIMAForecaster(ts, alpha);
			// Removed because WCF/TBATS is not integrated yet
			// case TBATS:
			// return new TBATSForecaster(ts, alpha);
		case INACT:
			return null;
		default:
			return null;
		}
	}

	/**
	 * 
	 * @return calculated anomaly
	 */
	public IAnomalyCalculator<Double> getAnomalyCalculator() {
		return new SimpleAnomalyCalculator();
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
			final ITimeSeries<Double> tsLastX = new TimeSeries<Double>(newStartTime, ts.getDeltaTime(), ts.getDeltaTimeUnit(), ts.getCapacity());
			tsLastX.appendAll(b);
			return tsLastX;
		} else {
			return ts;
		}
	}
}
