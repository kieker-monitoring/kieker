/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.opad.experimentModel;

/**
 * Output model with added timestamp property.
 * 
 * @author Thomas Duellmann
 *
 * @since 1.11
 */
public class DataOutputModelWithTimestamp extends DataOutputModel{
	private double measurement;
	private double forecast;
	private int confidence;
	private double confidenceUpper;
	private double confidenceLower;
	private String forecaster;
	private double mase;
	private long timestamp;

	public DataOutputModelWithTimestamp(final double measurement, final double forecast, final int confidence, final double confidenceUpper,
			final double confidenceLower, final String forecaster, final double mase, final long timestamp) {
		super(measurement,forecast,confidence,confidenceUpper,confidenceLower,forecaster,mase);
		this.timestamp = timestamp;
	}

	public double getForecast() {
		return this.forecast;
	}

	public void setForecast(final double forecast) {
		this.forecast = forecast;
	}

	public int getConfidence() {
		return this.confidence;
	}

	public void setConfidence(final int confidence) {
		this.confidence = confidence;
	}

	public double getMeasurement() {
		return this.measurement;
	}

	public void setMeasurement(final double measurement) {
		this.measurement = measurement;
	}

	public double getConfidenceUpper() {
		return this.confidenceUpper;
	}

	public void setConfidenceUpper(final double confidenceUpper) {
		this.confidenceUpper = confidenceUpper;
	}

	public double getConfidenceLower() {
		return this.confidenceLower;
	}

	public void setConfidenceLower(final double confidenceLower) {
		this.confidenceLower = confidenceLower;
	}

	public String getForecaster() {
		return this.forecaster;
	}

	public void setForecaster(final String forecaster) {
		this.forecaster = forecaster;
	}

	public double getMase() {
		return this.mase;
	}

	public void setMase(final double mase) {
		this.mase = mase;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}
}
