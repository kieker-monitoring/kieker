package kieker.tools.tslib.forecast.cs;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractRForecaster;

/**
 * 
 * @author Nikolas Herbst
 * 
 *         Cubic splines are fitted to the univariate time series data to obtain
 *         a trend estimate and linear forecast function.
 *         Prediction intervals are constructed by use of a likelihood approach for
 *         estimation of smoothing parameters. The cubic splines method can be mapped to
 *         an ARIMA 022 stochastic process model with a restricted parameter space.
 * 
 *         Overhead below 100ms for less than 30 values (more values do not sig. improve accuracy)
 */
public class CSForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "splinef";
	private final String[] emptyString = new String[0];

	// private final static ForecastMethod strategy = ForecastMethod.CS;
	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public CSForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, CSForecaster.MODEL_FUNC_NAME, CSForecaster.FORECAST_FUNC_NAME, ForecastMethod.CS);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public CSForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, CSForecaster.MODEL_FUNC_NAME, CSForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.CS);
	}

	@Override
	protected String[] getModelFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}
}
