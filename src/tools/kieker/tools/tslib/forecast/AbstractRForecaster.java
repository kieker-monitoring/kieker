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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.REngine.REXPLogical;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.util.RBridgeControl;

/**
 * Convenience class to implement an {@link IForecaster} with R.
 *
 * @since 1.10
 * @author Andre van Hoorn, Nikolas Herbst, Andreas Eberlein, Tobias Rudolph, Thomas Duellmann
 *
 */
public abstract class AbstractRForecaster extends AbstractForecaster<Double> {

	private static final Log LOG = LogFactory.getLog(AbstractRForecaster.class);

	private static final RBridgeControl RBRIDGE = RBridgeControl.getInstance();
	private static boolean forecastPackageAvailable;
	private final String modelFunc;
	private final String forecastFunc;
	private final ForecastMethod strategy;

	/**
	 * Acquire an instance of the {@link RBridgeControl} once.
	 */
	static {
		final Object forecastPackageLoadResult = AbstractRForecaster.RBRIDGE.evalWithR("require(forecast)");
		AbstractRForecaster.setForecastModuleAvailableAndLoadedFlag(forecastPackageLoadResult);
	}

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries
	 *
	 * @param modelFunc
	 *            modelFunction
	 * @param forecastFunc
	 *            forecastfunction
	 * @param strategy
	 *            FC strategy
	 */
	public AbstractRForecaster(final ITimeSeries<Double> historyTimeseries, final String modelFunc,
			final String forecastFunc, final ForecastMethod strategy) {
		super(historyTimeseries);
		this.modelFunc = modelFunc;
		this.forecastFunc = forecastFunc;
		this.strategy = strategy;

		if (!forecastPackageAvailable) {
			this.logForecastModuleNotAvailableOrLoaded();
		}
	}

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries
	 * @param modelFunc
	 *            modelFunction
	 * @param forecastFunc
	 *            forecastfunction
	 * @param confidenceLevel
	 *            value of confedenclevel
	 *
	 * @param strategy
	 *            FC strategy
	 */
	public AbstractRForecaster(final ITimeSeries<Double> historyTimeseries, final String modelFunc,
			final String forecastFunc, final int confidenceLevel, final ForecastMethod strategy) {
		super(historyTimeseries, confidenceLevel);
		this.modelFunc = modelFunc;
		this.forecastFunc = forecastFunc;
		this.strategy = strategy;

		if (!forecastPackageAvailable) {
			this.logForecastModuleNotAvailableOrLoaded();
		}
	}

	private static void setForecastModuleAvailableAndLoadedFlag(final Object forecastPackageLoadResult) {
		if (forecastPackageLoadResult instanceof REXPLogical) {
			final REXPLogical returnValue = (REXPLogical) forecastPackageLoadResult;
			final boolean hasAttr = returnValue.hasAttribute("attr");
			final boolean[] istrue = returnValue.isTRUE();

			if (!hasAttr && (istrue.length > 0) && istrue[0]) {
				forecastPackageAvailable = true;
				return;
			}
		}
		forecastPackageAvailable = false;
	}

	private void logForecastModuleNotAvailableOrLoaded() {
		final IllegalStateException ise = new IllegalStateException("\"forecast\" package could not be loaded in R.");
		LOG.error("Could not load \"forecast\" package in R. Perhaps it is not installed in R?", ise);
	}

	/**
	 * @param numForecastSteps
	 *            amount of to calculate FC steps
	 * @return ForecastResult
	 */
	@Override
	public final IForecastResult forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();

		final String varNameValues = RBridgeControl.uniqueVarname();
		final String varNameModel = RBridgeControl.uniqueVarname();
		final String varNameForecast = RBridgeControl.uniqueVarname();

		final List<Double> allHistory = new ArrayList<Double>(history.getValues());
		final Double[] histValuesNotNull = AbstractRForecaster.removeNullValues(allHistory);
		final double[] values = ArrayUtils.toPrimitive(histValuesNotNull);

		// 0. Assign values to temporal variable

		AbstractRForecaster.RBRIDGE.assign(varNameValues, values);

		if (history.getFrequency() != 0) {
			if (this.strategy != ForecastMethod.ARIMA) {
				// frequency for time series object in R --> needed for MASE calculation.
				AbstractRForecaster.RBRIDGE.toTS(varNameValues, history.getFrequency());
			} else {
				AbstractRForecaster.RBRIDGE.toTS(varNameValues);
			}
		}

		// 1. Compute stochastic model for forecast

		if (this.modelFunc == null) {
			// In this case, the values are the model ...
			AbstractRForecaster.RBRIDGE.assign(varNameModel, values);
			if (history.getFrequency() != 0) {
				if (this.strategy != ForecastMethod.ARIMA) {
					// frequency for time series object in R --> needed for MASE calculation.
					AbstractRForecaster.RBRIDGE.toTS(varNameValues, history.getFrequency());
				} else {
					AbstractRForecaster.RBRIDGE.toTS(varNameValues);
				}
			}
		} else {
			final String[] additionalModelParams = this.getModelFuncParams();

			final StringBuffer params = new StringBuffer();
			params.append(varNameValues);
			if (null != additionalModelParams) {
				for (final String param : additionalModelParams) {
					params.append(',');
					params.append(param);
				}
			}
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <<- %s(%s)", varNameModel, this.modelFunc, params));
		}
		// remove temporal variable:
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameValues));

		// 2. Perform forecast based on stochastic model

		if (this.getConfidenceLevel() == 0) {
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <<- %s(%s, h=%d)", varNameForecast, this.forecastFunc, varNameModel,
					numForecastSteps));
		} else {
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <<- %s(%s, h=%d, level=c(%d))", varNameForecast, this.forecastFunc, varNameModel,
					numForecastSteps, this.getConfidenceLevel()));
		}

		final double[] lowerValues = AbstractRForecaster.RBRIDGE.eDblArr(this.lowerOperationOnResult(varNameForecast));
		final double[] forecastValues = AbstractRForecaster.RBRIDGE.eDblArr(this.forecastOperationOnResult(varNameForecast));
		final double[] upperValues = AbstractRForecaster.RBRIDGE.eDblArr(this.upperOperationOnResult(varNameForecast));

		// 3. Calculate Forecast Quality Metric

		double fcQuality = Double.NaN;
		if (forecastValues.length > 1) {
			if ((this.modelFunc == null)) { // Re-enable when TBATS included: || (this.strategy == ForecastMethod.TBATS)
				fcQuality = AbstractRForecaster.RBRIDGE.eDbl("accuracy(" + varNameForecast + ")[6]");
			} else {
				fcQuality = AbstractRForecaster.RBRIDGE.eDbl("accuracy(" + varNameModel + ")[6]");
			}
		}

		// remove temporal variable:
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameModel));
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameValues));
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameForecast));

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

		return new ForecastResult(tsForecast, this.getTsOriginal(), this.getConfidenceLevel(), fcQuality, tsLower, tsUpper, this.strategy);
	}

	/**
	 * @param varNameForecast
	 *            Name FC
	 * @return string loweropresult
	 */
	protected String lowerOperationOnResult(final String varNameForecast) {
		return String.format("%s$lower", varNameForecast);
	}

	/**
	 * @param varNameForecast
	 *            name fc
	 * @return string upperopresult
	 */
	protected String upperOperationOnResult(final String varNameForecast) {
		return String.format("%s$upper", varNameForecast);
	}

	/**
	 * @param varNameForecast
	 *            name FC
	 * @return string operation result
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

	/**
	 *
	 * @param allHistory
	 *            List there null values should be deleted in this function
	 * @return List/Array with no NullValues
	 */
	public static Double[] removeNullValues(final List<Double> allHistory) {
		final List<Double> newList = new ArrayList<Double>();
		for (final Object obj : allHistory) {
			if ((null != obj) && (obj instanceof Double)) {
				newList.add((Double) obj);
			}
		}
		return newList.toArray(new Double[newList.size()]);
	}
}
