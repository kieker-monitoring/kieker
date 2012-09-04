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

package kieker.tools.tslib.forecast.ses;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractForecaster;
import kieker.tools.tslib.forecast.ForecastResult;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.mean.MeanForecasterJava;
import kieker.tools.util.RBridgeControl;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class SESRForecaster extends AbstractForecaster<Double> {

	public SESRForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries);
	}

	public IForecastResult<Double> forecast(final int n) {
		final ITimeSeries<Double> history = this.getTsOriginal();
		final ITimeSeries<Double> tsFC = super.prepareForecastTS();

		final List<Double> allHistory = new ArrayList<Double>(history.getValues());
		final Double[] histValuesNotNull = MeanForecasterJava.removeNullValues(allHistory);
		final double[] values = ArrayUtils.toPrimitive(histValuesNotNull);

		final RBridgeControl rBridge = RBridgeControl.getInstance(new File("r_scripts"));
		rBridge.e("initTS()");
		rBridge.assign("ts_history", values);
		final double[] pred = rBridge.eDblArr("getForecast(ts_history)");

		if (pred.length > 0) {
			tsFC.append(pred[0]);
		}
		return new ForecastResult<Double>(tsFC, this.getTsOriginal());
	}
}

/*
 * This was the intention to do it (with avh's code)
 * 
 * r.loadLibrary("tseries");
 * r.loadLibrary("stats");
 * 
 * r.assign("points", values);
 * 
 * try {
 * Thread.sleep(1300);
 * } catch (InterruptedException e1) {
 * // TODO Auto-generated catch block
 * e1.printStackTrace();
 * }
 * 
 * try {
 * // REXP hwResult = r.rEvalSync("ses <- HoltWinters(points, beta = FALSE, gamma = FALSE)");
 * REXP hwResult = r.rEvalSync("HoltWinters");
 * r.assign("ses", hwResult);
 * REXP result = r.rEvalSync("predict(ses, " + n + ", prediction.interval = TRUE)");
 * } catch (REngineFacadeEvalException e) {
 * e.printStackTrace();
 * }
 */
