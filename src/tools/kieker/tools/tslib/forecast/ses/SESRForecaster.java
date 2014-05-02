package kieker.tools.tslib.forecast.ses;

import kieker.tools.tslib.ForecastMethod;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.forecast.AbstractRForecaster;

/**
 * 
 * @author Nikolas Herbst
 *         Generalization of MA by using weights according to the exponential function
 *         to give higher weight to more recent values.
 *         1st step: estimation of parameters for weights/exp. function
 *         2nd step: calculation of weighted averages as point forecast
 */
public class SESRForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = "ets"; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "forecast";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public SESRForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, SESRForecaster.MODEL_FUNC_NAME, SESRForecaster.FORECAST_FUNC_NAME, ForecastMethod.SES);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * 
	 * @param confidenceLevel
	 *            confidenceLevel
	 */
	public SESRForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, SESRForecaster.MODEL_FUNC_NAME, SESRForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.SES);
	}

	@Override
	protected String[] getModelFuncParams() {
		return new String[] { "model=\"ANN\"" };
	}

	@Override
	/**
	 * From R Forecast documentation:
	 * Usually a three-character string identifying method
	 * using the framework terminology of Hyndman et al. (2002) and Hyndman et al. (2008).
	 * The first letter denotes the error type ("A", "M" or "Z");
	 * the second letter denotes the trend type ("N","A","M" or "Z");
	 * and the third letter denotes the season type ("N","A","M" or "Z").
	 * In all cases, "N"=none, "A"=additive, "M"=multiplicative and "Z"=automatically selected.
	 * So, for example, "ANN" is simple exponential smoothing with additive errors,
	 * "MAM" is multiplicative Holt-Winters' method with multiplicative errors, and so on.
	 * It is also possible for the model to be equal to the output from a previous call to ets.
	 * In this case, the same model is fitted to y without re-estimating any parameters.

	 * no additional params required by this predictor
	 * @return emptyString array
	 */
	protected String[] getForecastFuncParams() {
		return this.emptyString; // no additional params required by this predictor
	}
}
