/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast.mean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractForecaster;
import kieker.tools.opad.timeseries.forecast.ForecastResult;
import kieker.tools.opad.timeseries.forecast.IForecastResult;

/**
 * A Java-based time series forecaster which computes a forecast based on the mean value of the historic values.
 *
 * @author Andre van Hoorn
 * @since 1.10
 */
public class MeanForecasterJava extends AbstractForecaster<Double> {

	/**
	 *
	 * @param historyTimeseries
	 *            TimeSeries
	 */
	public MeanForecasterJava(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries);
	}

	/**
	 * @param numForecastSteps
	 *            number of values the forecaster is going to forecast
	 *
	 * @return Forecast Result
	 */
	@Override
	public IForecastResult forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();
		final ITimeSeries<Double> tsFC = this.prepareForecastTS();

		final List<Double> allHistory = new ArrayList<Double>(history.getValues());
		final Double[] histValuesNotNull = MeanForecasterJava.removeNullValues(allHistory);
		final double mean = StatUtils.mean(ArrayUtils.toPrimitive(histValuesNotNull));
		final Double[] forecastValues = new Double[numForecastSteps];
		Arrays.fill(forecastValues, mean);

		tsFC.appendAll(forecastValues);
		return new ForecastResult(tsFC, this.getTsOriginal(), ForecastMethod.MEAN);
	}

	/**
	 *
	 * @param allHistory
	 *            List there null values should deltet in this function
	 * @return List/Array with no NullValues
	 */
	public static Double[] removeNullValues(final List<Double> allHistory) {
		final List<Double> newList = new ArrayList<Double>();

		for (final Object obj : allHistory) {
			if ((null != obj) && (obj instanceof Double) && !Double.isNaN((Double) obj)) {
				newList.add((Double) obj);
			}
		}
		return newList.toArray(new Double[newList.size()]);
	}

	@Override
	protected boolean supportsConfidence() {
		// Does not support confidence;
		return false;
	}
}
