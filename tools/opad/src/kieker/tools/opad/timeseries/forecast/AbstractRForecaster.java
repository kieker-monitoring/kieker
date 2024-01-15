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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.REngine.REXPLogical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.util.InvalidREvaluationResultException;
import kieker.tools.util.RBridgeControl;

/**
 * Convenience class to implement an {@link IForecaster} with R.
 *
 * @since 1.10
 * @author Andre van Hoorn, Nikolas Herbst, Andreas Eberlein, Tobias Rudolph, Thomas Duellmann
 *
 */
public abstract class AbstractRForecaster extends AbstractForecaster<Double> {

	public static final int MIN_TS_SIZE_DEFAULT = 5;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRForecaster.class);
	private static final RBridgeControl RBRIDGE = RBridgeControl.getInstance();
	private static boolean forecastPackageAvailable;
	private final String modelFunc;
	private final String forecastFunc;
	private final ForecastMethod strategy;

	/**
	 * Acquire an instance of the {@link RBridgeControl} once.
	 */
	static {
		Object forecastPackageLoadResult = null;
		try {
			forecastPackageLoadResult = AbstractRForecaster.RBRIDGE.evalWithR("require(forecast)");
		} catch (final InvalidREvaluationResultException e) {
			LOGGER.warn("An exception occurred in R", e);
		} finally {
			AbstractRForecaster.setForecastModuleAvailableAndLoadedFlag(forecastPackageLoadResult);
		}

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

	protected IForecastResult createNaNForecast(final ITimeSeries<Double> timeseries, final int numForecastSteps) {
		final ITimeSeries<Double> tsForecast = this.prepareForecastTS();
		final ITimeSeries<Double> tsLower = this.prepareForecastTS();
		final ITimeSeries<Double> tsUpper = this.prepareForecastTS();
		final Double fcQuality = Double.NaN;

		final Double[] nanArray = new Double[numForecastSteps];
		Arrays.fill(nanArray, Double.NaN);
		tsForecast.appendAll(nanArray);
		tsLower.appendAll(nanArray);
		tsUpper.appendAll(nanArray);

		return new ForecastResult(tsForecast, this.getTsOriginal(), this.getConfidenceLevel(), fcQuality, tsLower, tsUpper, this.strategy);
	}

	private static void setForecastModuleAvailableAndLoadedFlag(final Object forecastPackageLoadResult) {
		if ((forecastPackageLoadResult != null) && (forecastPackageLoadResult instanceof REXPLogical)) {
			final REXPLogical returnValue = (REXPLogical) forecastPackageLoadResult;
			final boolean hasAttr = returnValue.hasAttribute("attr");
			final boolean[] isTrue = returnValue.isTRUE();

			if (!hasAttr && (isTrue.length > 0) && isTrue[0]) {
				forecastPackageAvailable = true;
				return;
			}
		}
		forecastPackageAvailable = false;
	}

	private void logForecastModuleNotAvailableOrLoaded() {
		final IllegalStateException ise = new IllegalStateException("\"forecast\" package could not be loaded in R.");
		LOGGER.error("Could not load \"forecast\" package in R. Perhaps it is not installed in R?", ise);
	}

	/**
	 * @param numForecastSteps
	 *            amount of to calculate FC steps
	 * @return ForecastResult
	 */
	@Override
	public final IForecastResult forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();
		IForecastResult forecast = null;
		if (this.satisfiesInputTSRequirements(history)) {
			try {
				forecast = this.forecastWithR(numForecastSteps);
			} catch (final InvalidREvaluationResultException exception) {
				LOGGER.warn("Exception occured when forecast with R", exception);
			}
		}

		if (forecast == null) {
			LOGGER.warn("Null result for forecast. Falling back to Double.NaN result.");
			forecast = this.createNaNForecast(history, numForecastSteps);
		}
		return forecast;

	}

	private ForecastResult forecastWithR(final int numForecastSteps) throws InvalidREvaluationResultException {
		final ITimeSeries<Double> history = this.getTsOriginal();
		final ITimeSeries<Double> tsForecast = this.prepareForecastTS();

		final String varNameValues = RBridgeControl.uniqueVarname();
		final String varNameModel = RBridgeControl.uniqueVarname();
		final String varNameForecast = RBridgeControl.uniqueVarname();

		final List<Double> allHistory = new ArrayList<>(history.getValues());
		final Double[] histValuesNotNull = AbstractRForecaster.removeNullValues(allHistory);
		final double[] values = ArrayUtils.toPrimitive(histValuesNotNull);

		double fcQuality = Double.NaN;
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
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <- %s(%s)", varNameModel, this.modelFunc, params));
		}
		// remove temporal variable:
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameValues));

		// 2. Perform forecast based on stochastic model

		if ((this.getConfidenceLevel() == 0) || !this.supportsConfidence()) {
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <- %s(%s, h=%d)", varNameForecast, this.forecastFunc, varNameModel,
					numForecastSteps));
		} else {
			AbstractRForecaster.RBRIDGE.evalWithR(String.format("%s <- %s(%s, h=%d, level=c(%d))", varNameForecast, this.forecastFunc, varNameModel,
					numForecastSteps, this.getConfidenceLevel()));
		}

		final double[] forecastValues = AbstractRForecaster.RBRIDGE.eDblArr(this.forecastOperationOnResult(varNameForecast));

		// 3. Calculate Forecast Quality Metric

		if (forecastValues.length > 1) {
			if ((this.modelFunc == null)) { // Re-enable when TBATS included: || (this.strategy == ForecastMethod.TBATS)
				fcQuality = AbstractRForecaster.RBRIDGE.eDbl("accuracy(" + varNameForecast + ")[6]");
			} else {
				fcQuality = AbstractRForecaster.RBRIDGE.eDbl("accuracy(" + varNameModel + ")[6]");
			}
		}

		tsForecast.appendAll(ArrayUtils.toObject(forecastValues));
		final ITimeSeries<Double> tsLower;
		final ITimeSeries<Double> tsUpper;

		if ((this.getConfidenceLevel() == 0) || !this.supportsConfidence()) {
			tsLower = tsForecast;
			tsUpper = tsForecast;
		} else {
			final double[] lowerValues = AbstractRForecaster.RBRIDGE.eDblArr(this.lowerOperationOnResult(varNameForecast));
			final double[] upperValues = AbstractRForecaster.RBRIDGE.eDblArr(this.upperOperationOnResult(varNameForecast));
			tsLower = this.prepareForecastTS();
			tsLower.appendAll(ArrayUtils.toObject(lowerValues));
			tsUpper = this.prepareForecastTS();
			tsUpper.appendAll(ArrayUtils.toObject(upperValues));
		}

		// remove temporal variable:
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameModel));
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameValues));
		AbstractRForecaster.RBRIDGE.evalWithR(String.format("rm(%s)", varNameForecast));

		return new ForecastResult(tsForecast, this.getTsOriginal(), this.getConfidenceLevel(), fcQuality, tsLower, tsUpper, this.strategy);
	}

	/**
	 * Checks whether the requirements for the input TS are met.
	 * This default implementation checks whether the length of the time series is greater or equal to {@value #MIN_TS_SIZE_DEFAULT}.
	 * This method can be overridden by any forecaster if more specific requirements are needed.
	 *
	 * @param timeSeries
	 *            time series
	 *
	 * @return returns true when requirements are met
	 */
	protected boolean satisfiesInputTSRequirements(final ITimeSeries<Double> timeSeries) {
		return timeSeries.size() >= MIN_TS_SIZE_DEFAULT;
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
	 * Returns additional parameters to be appended to the call of the R forecast model.
	 *
	 * @return the parameters or null if none
	 */
	protected abstract String[] getModelFuncParams();

	/**
	 * Returns additional parameters to be appended to the call of the R forecast function.
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
		final List<Double> newList = new ArrayList<>();
		for (final Object obj : allHistory) {
			if ((null != obj) && (obj instanceof Double)) {
				newList.add((Double) obj);
			}
		}
		return newList.toArray(new Double[newList.size()]);
	}
}
