package kieker.tools.tslib.forecast.naive;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractRForecaster;

/**
 * 
 * @author Nikolas Herbst
 *         The naïve forecast considers only the
 *         value of the most recent observation assuming that this
 *         value has the highest probability for the next forecast point.
 * 
 *         Horizon: very short term forecast (1-2 points)
 *         Overhead: nearly none O(1)
 */
public class NaiveForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "rwf";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public NaiveForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, NaiveForecaster.MODEL_FUNC_NAME, NaiveForecaster.FORECAST_FUNC_NAME, ForecastMethod.NAIVE);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public NaiveForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, NaiveForecaster.MODEL_FUNC_NAME, NaiveForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.NAIVE);
	}

	@Override
	protected String[] getModelFuncParams() {
		return this.emptyString; // no additional params required by this predictor
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString; // no additional params required by this predictor
	}
}
