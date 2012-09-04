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

package kieker.tools.tslib.forecast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.mean.MeanForecasterJava;
import kieker.tools.util.RBridgeControl;

/**
 * Convenience class to implement an {@link IForecaster} with R.
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractRForecaster extends AbstractForecaster<Double> {
	/**
	 * Acquire an instance of the {@link RBridgeControl} once
	 */
	private final static RBridgeControl rBridge = RBridgeControl.getInstance(new File("r_scripts"));
	static {
		AbstractRForecaster.rBridge.e("require(forecast)");
	}

	private final String modelFunc;
	private final String forecastFunc;

	public AbstractRForecaster(final ITimeSeries<Double> historyTimeseries, final String modelFunc,
			final String forecastFunc) {
		super(historyTimeseries);
		this.modelFunc = modelFunc;
		this.forecastFunc = forecastFunc;
	}

	public AbstractRForecaster(final ITimeSeries<Double> historyTimeseries, final String modelFunc,
			final String forecastFunc, final int confidenceLevel) {
		super(historyTimeseries, confidenceLevel);
		this.modelFunc = modelFunc;
		this.forecastFunc = forecastFunc;
	}

	public final IForecastResult<Double> forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();

		final String varNameValues = RBridgeControl.uniqueVarname();
		final String varNameModel = RBridgeControl.uniqueVarname();
		final String varNameForecast = RBridgeControl.uniqueVarname();

		// final double[] values = ArrayUtils.toPrimitive(history.getValues().toArray(new Double[] {}));

		final List<Double> allHistory = new ArrayList<Double>(history.getValues());
		final Double[] histValuesNotNull = MeanForecasterJava.removeNullValues(allHistory);
		final double[] values = ArrayUtils.toPrimitive(histValuesNotNull);

		/*
		 * 0. Assign values to temporal variable
		 */
		AbstractRForecaster.rBridge.assign(varNameValues, values);

		/*
		 * 1. Compute stochastic model for forecast
		 */
		if (this.modelFunc == null) {
			// In this case, the values are the model ...
			AbstractRForecaster.rBridge.assign(varNameModel, values);
		} else {
			final String[] additionalModelParams = this.getModelFuncParams();

			final StringBuffer params = new StringBuffer();
			params.append(varNameValues);
			if (null != additionalModelParams) {
				for (final String param : additionalModelParams) {
					params.append(",");
					params.append(param);
				}
			}
			AbstractRForecaster.rBridge.e(String.format("%s<<-%s(%s)", varNameModel, this.modelFunc, params));
		}
		// remove temporal variable:
		AbstractRForecaster.rBridge.e(String.format("rm(%s)", varNameValues));

		/*
		 * 2. Perform forecast based on stochastic model
		 */
		final String[] additionalForecastParams = this.getModelFuncParams();
		// TODO: append additionalForecastParams to call
		AbstractRForecaster.rBridge.e(String.format("%s<<-%s(%s,h=%d,level=c(%d))", varNameForecast, this.forecastFunc, varNameModel,
				numForecastSteps, this.getConfidenceLevel()));
		// remove temporal variable:
		AbstractRForecaster.rBridge.e(String.format("rm(%s)", varNameModel));

		// final double mean = MeanForecasterR.rBridge.eDbl(String.format("mean(c(%s))", varNameValues));

		final double[] lowerValues = AbstractRForecaster.rBridge.eDblArr(this.lowerOperationOnResult(varNameForecast));
		final double[] forecastValues = AbstractRForecaster.rBridge.eDblArr(this.forecastOperationOnResult(varNameForecast));
		final double[] upperValues = AbstractRForecaster.rBridge.eDblArr(this.upperOperationOnResult(varNameForecast));
		AbstractRForecaster.rBridge.e(String.format("rm(%s)", varNameForecast));

		final ITimeSeries<Double> tsForecast = this.prepareForecastTS();
		final ITimeSeries<Double> tsLower;
		final ITimeSeries<Double> tsUpper;
		tsForecast.appendAll(ArrayUtils.toObject(forecastValues));

		if (this.getConfidenceLevel() == 0) {
			tsLower = tsForecast;
			tsUpper = tsForecast;
		} else {
			tsLower = this.prepareForecastTS();
			tsLower.appendAll(ArrayUtils.toObject(lowerValues));
			tsUpper = this.prepareForecastTS();
			tsUpper.appendAll(ArrayUtils.toObject(upperValues));
		}

		return new ForecastResult<Double>(tsForecast, this.getTsOriginal(), this.getConfidenceLevel(), tsLower, tsUpper);
	}

	/**
	 * @param varNameForecast
	 * @return
	 */
	protected String lowerOperationOnResult(final String varNameForecast) {
		return String.format("%s$lower", varNameForecast);
	}

	/**
	 * @param varNameForecast
	 * @return
	 */
	protected String upperOperationOnResult(final String varNameForecast) {
		return String.format("%s$upper", varNameForecast);
	}

	/**
	 * @param varNameForecast
	 * @return
	 */
	protected String forecastOperationOnResult(final String varNameForecast) {
		return String.format("%s$mean", varNameForecast);
	}

	/**
	 * Returns additional parameters to be appended to the call of the R function {@link #getModelFuncName()}.
	 * 
	 * @return the parameters or null if none
	 */
	protected abstract String[] getModelFuncParams();

	/**
	 * Returns additional parameters to be appended to the call of the R function {@link #getForecastFuncName()}.
	 * 
	 * @return the parameters or null if none
	 */
	protected abstract String[] getForecastFuncParams();
}
