/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.tools.tslib.forecast.mean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.StatUtils;

import kicker.tools.tslib.ITimeSeries;
import kicker.tools.tslib.forecast.AbstractForecaster;
import kicker.tools.tslib.forecast.ForecastResult;
import kicker.tools.tslib.forecast.IForecastResult;

/**
 * A Java-based time series forecaster which computes a forecast based on the mean value of the historic values.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.9
 */
public class MeanForecasterJava extends AbstractForecaster<Double> {

	public MeanForecasterJava(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries);
	}

	@Override
	public IForecastResult<Double> forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();
		final ITimeSeries<Double> tsFC = this.prepareForecastTS();

		final List<Double> allHistory = new ArrayList<Double>(history.getValues());
		final Double[] histValuesNotNull = MeanForecasterJava.removeNullValues(allHistory);
		final double mean = StatUtils.mean(ArrayUtils.toPrimitive(histValuesNotNull));

		final Double[] forecastValues = new Double[numForecastSteps];
		Arrays.fill(forecastValues, mean);

		tsFC.appendAll(forecastValues);

		// TODO #1217: compute confidence interval and set this value along with upper and lower time series

		return new ForecastResult<Double>(tsFC, this.getTsOriginal());
	}

	public static Double[] removeNullValues(final List<Double> allHistory) {
		final List<Double> newList = new ArrayList<Double>();

		for (final Object obj : allHistory) {
			if (obj instanceof Double) {
				newList.add((Double) obj);
			}
		}

		return newList.toArray(new Double[newList.size()]);
	}
}
