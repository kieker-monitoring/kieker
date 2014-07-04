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

package kieker.tools.tslib.forecast.historicdata;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.StatUtils;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractForecaster;
import kieker.tools.tslib.forecast.ForecastResult;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * This forecaster computes a prediction based on a historic data set that is searched in a long term data base.
 * The prediction searches for a time series point in the past. This point is determined with the help of a
 * seasonal pattern (distance, timeunit). Around the found time series point a new time series window is established,
 * the values of the time series window are aggregated and used as prediction.
 * 
 * @since 1.10
 * @author Tom Frotscher
 * 
 */
public class PatternCheckingForecaster extends AbstractForecaster<Double> {

	private final long distance;
	private final TimeUnit timeunit;
	private final TimeUnit windowLengthUnit;
	private final DBCollection coll;
	private final int timeSeriesWindowCapacity;
	private final NamedDoubleTimeSeriesPoint input;

	private DBCursor resultCursor;

	/**
	 * Creates a net historic data forecaster.
	 * 
	 * @param historyTimeseries
	 *            actual sliding window
	 * @param dist
	 *            distance of the pattern
	 * @param tunit
	 *            time unit of the pattern
	 * @param coll
	 *            database collection
	 * @param windowLenghthUnit
	 *            time unit of the window that is used on the historic data within this forecaster
	 * @param in
	 *            new input value value
	 * @param tsWindowCap
	 *            time series window capacity of the sliding window, also defining the capacity of the new time series windows in the past
	 */
	public PatternCheckingForecaster(final ITimeSeries<Double> historyTimeseries, final long dist, final TimeUnit tunit, final TimeUnit windowLenghthUnit,
			final DBCollection coll, final int tsWindowCap, final NamedDoubleTimeSeriesPoint in) {
		super(historyTimeseries);
		this.distance = dist;
		this.timeunit = tunit;
		this.windowLengthUnit = windowLenghthUnit;
		this.coll = coll;
		this.timeSeriesWindowCapacity = tsWindowCap;
		this.input = in;
	}

	/**
	 * Proceeds the forecast of the historic data forecaster.
	 * 
	 * @param numForecastSteps
	 *            Number of steps predicted in the future
	 * 
	 * @return
	 *         forecast result
	 */
	@Override
	public IForecastResult forecast(final int numForecastSteps) {
		final ITimeSeries<Double> tsFC = this.prepareForecastTS();

		this.isForecastWindowFromDB();
		final double alternativeReferenceValue = this.extractAlternativeReferenceModel(this.resultCursor);

		final Double[] forecastValues = new Double[numForecastSteps];
		Arrays.fill(forecastValues, alternativeReferenceValue);

		tsFC.appendAll(forecastValues);
		return new ForecastResult(tsFC, this.getTsOriginal(), ForecastMethod.PatternChecking);
	}

	/**
	 * Get the data from the data base that matches to the configured pattern. Is also used to check
	 * whether the historic forecaster can be used (there is a corresponding time point in the database)
	 * or not.
	 * 
	 * @return
	 *         true if corresponding time series point in the past can be found, else false
	 */

	public boolean isForecastWindowFromDB() {
		// Calculate the corresponding time stamp in the past, that may contain corresponding data according to the pattern
		final long distantTSPoint = this.input.getTime() - TimeUnit.MILLISECONDS.convert(this.distance, this.timeunit);
		// Calculate the time series window size in milliseconds
		final long interval = TimeUnit.MILLISECONDS.convert(this.timeSeriesWindowCapacity, this.windowLengthUnit);
		// Find corresponding values in the long term database within the time series window in the past
		final BasicDBObject query = new BasicDBObject("timestamp", new BasicDBObject("$gte", distantTSPoint - interval).
				append("$lte", distantTSPoint)).
				append("anomaly", false).
				append("application", this.input.getName());
		this.resultCursor = this.coll.find(query);
		return this.resultCursor.size() > 0;
	}

	/**
	 * This methods extract a value of the alternative reference model. It calculates the mean of all
	 * values of the found corresponding time window.
	 * 
	 * @param cursor
	 *            DB reference holding the corresponding time window
	 * @return
	 *         New value of the alternative reference model
	 */
	private double extractAlternativeReferenceModel(final DBCursor cursor) {
		final double[] values = new double[cursor.size()];
		try {
			for (int i = 0; i < cursor.size(); i++) {
				final JSONObject obj = new JSONObject(cursor.next().toString());
				values[i] = obj.getDouble("value");
			}
		} finally {
			cursor.close();
		}
		return StatUtils.mean(values);
	}
}
