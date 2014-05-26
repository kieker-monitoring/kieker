package kieker.tools.tslib.forecast.croston;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractRForecaster;

/**
 * Intermittent Demand Forecasting.
 * 
 * @author Nikolas Herbst
 *         Decomposition of the time series that contains zero values into
 *         two separate sequences: a non-zero valued time series and a second
 *         that contains the time intervals of zero values. Independent
 *         forecast using SES and combination of the two independent forecasts.
 *         No confidence intervals are computed due to no consistent underlying stochastic model.
 */
public class CrostonForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "croston";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public CrostonForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, CrostonForecaster.MODEL_FUNC_NAME, CrostonForecaster.FORECAST_FUNC_NAME, ForecastMethod.CROSTON);
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
