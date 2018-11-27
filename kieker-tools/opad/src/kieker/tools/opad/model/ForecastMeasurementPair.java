/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.model;

/**
 *
 * @author Tillmann Carlos Bielefeld, Thomas Duellmann
 * @since 1.10
 *
 * @since 1.9
 */
public class ForecastMeasurementPair implements IForecastMeasurementPair {

	private final String name;
	private final Double forecast;
	private final Double measurement;
	private final Double mase; // MASE = Mean Absolute Scaled Error
	private final long time;
	private int confidenceLevel = -1;
	private double confidenceUpper = Double.NaN;
	private double confidenceLower = Double.NaN;

	/**
	 * Create a forecast measurement pair with confidence values.
	 *
	 * @param name
	 *            name of the measurement
	 * @param forecast
	 *            forecast value
	 * @param measurement
	 *            measurement value
	 * @param time
	 *            timestamp
	 * @param confidenceLevel
	 *            confidence value
	 * @param confidenceUpper
	 *            upper limit
	 * @param confidenceLower
	 *            lower limit
	 * @param mase
	 *            mase
	 */
	public ForecastMeasurementPair(final String name, final Double forecast, final Double measurement, final long time, final int confidenceLevel,
			final double confidenceUpper, final double confidenceLower, final Double mase) {
		this(name, forecast, measurement, time, mase);
		this.confidenceLevel = confidenceLevel;
		this.confidenceUpper = confidenceUpper;
		this.confidenceLower = confidenceLower;
	}

	/**
	 * Create a forecast measurement pair without confidence values.
	 *
	 * @param name
	 *            name of the measurement
	 * @param forecast
	 *            forecast value
	 * @param measurement
	 *            measurement value
	 * @param time
	 *            timestamp
	 * @param mase
	 *            mase
	 */
	public ForecastMeasurementPair(final String name, final Double forecast, final Double measurement, final long time, final Double mase) {
		this.name = name;
		this.forecast = forecast;
		this.measurement = measurement;
		this.time = time;
		this.mase = mase;
	}

	/**
	 * Create a forecast measurement pair without confidence values and mase.
	 *
	 * @param name
	 *            name of the measurement
	 * @param forecast
	 *            forecast value
	 * @param measurement
	 *            measurement value
	 * @param time
	 *            timestamp
	 */
	public ForecastMeasurementPair(final String name, final Double forecast, final Double measurement, final long time) {
		this(name, forecast, measurement, time, Double.NaN);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public Double getValue() {
		return this.measurement;
	}

	@Override
	public Double getForecasted() {
		return this.forecast;
	}

	public int getConfidenceLevel() {
		return this.confidenceLevel;
	}

	public double getConfidenceUpper() {
		return this.confidenceUpper;
	}

	public double getConfidenceLower() {
		return this.confidenceLower;
	}

	public Double getMASE() {
		return this.mase;
	}
}
