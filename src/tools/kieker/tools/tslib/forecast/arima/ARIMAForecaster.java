package kieker.tools.tslib.forecast.arima;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractRForecaster;

/**
 * An R-based time series forecaster, auto-arima model selection.
 * 
 * @author Andre van Hoorn + Nikolas Herbst
 * 
 *         The automated ARIMA model selection process of the R forecasting package starts
 *         with a complex estimation of an appropriate ARIMA(p, d, q)(P, D, Q)m model by using
 *         unit-root tests and an information criterions (like the AIC) in combination with
 *         a step-wise procedure for traversing a relevant model space.
 *         The selected ARIMA model is then fitted to the data to provide point forecasts
 *         and confidence intervals.
 */
public class ARIMAForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = "auto.arima"; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "forecast";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public ARIMAForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, ARIMAForecaster.MODEL_FUNC_NAME, ARIMAForecaster.FORECAST_FUNC_NAME, ForecastMethod.ARIMA);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public ARIMAForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, ARIMAForecaster.MODEL_FUNC_NAME, ARIMAForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.ARIMA);
	}

	@Override
	protected String[] getModelFuncParams() {
		return new String[] { "parallel=\"true\"" };
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString; // no additional params required by this predictor
	}
}
