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

package kieker.tools.opad.record;

import kieker.common.record.AbstractMonitoringRecord;

/**
 * Extended StorableDetectionResult class that extends the StorableDetectionResult class by anomaly threshold.
 * 
 * @author Thomas Düllmann
 * @since 1.10
 * 
 */
public class ExtendedStorableDetectionResult extends StorableDetectionResult {

	private static final long serialVersionUID = -5529586414782099554L;

	private static final int SIZE = TYPE_SIZE_STRING + TYPE_SIZE_DOUBLE + TYPE_SIZE_LONG + TYPE_SIZE_DOUBLE + TYPE_SIZE_DOUBLE + TYPE_SIZE_DOUBLE;

	private static final Class<?>[] TYPES = {
		String.class, // application
		double.class, //
		long.class,
		double.class,
		double.class,
		double.class,
	};

	private final double anomalyThreshold;

	/**
	 * Constructor.
	 * 
	 * @param hostAppOperationName
	 *            host, app and operation name of the instrumented environment (host+app:operation)
	 * @param latency
	 *            the latency the method has
	 * @param timestamp
	 *            timestamp of the measurement
	 * @param forecast
	 *            forecasted value
	 * @param score
	 *            calculated anomaly score
	 * @param anomalyThreshold
	 *            anomaly threshold
	 */
	public ExtendedStorableDetectionResult(
			final String hostAppOperationName, final double latency, final long timestamp,
			final double forecast, final double score, final double anomalyThreshold) {
		super(hostAppOperationName, latency, timestamp, forecast, score);
		this.anomalyThreshold = anomalyThreshold;
	}

	/**
	 * Constructor.
	 * 
	 * @param sdr
	 *            StorableDetectionResult object
	 * @param anomalyThreshold
	 *            anomaly threshold
	 */
	public ExtendedStorableDetectionResult(final StorableDetectionResult sdr, final double anomalyThreshold) {
		this(sdr.getApplication(), sdr.getValue(), sdr.getTimestamp(), sdr.getForecast(), sdr.getScore(), anomalyThreshold);
	}

	/**
	 * Constructor.
	 * 
	 * @param values
	 *            values of members as Object array
	 */
	public ExtendedStorableDetectionResult(final Object[] values) { // NOPMD
		super(values);
		AbstractMonitoringRecord.checkArray(values, ExtendedStorableDetectionResult.TYPES);
		this.anomalyThreshold = (Double) values[5];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return ExtendedStorableDetectionResult.TYPES.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.applicationName, this.value, this.timestamp, this.forecast, this.score };
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * Returns the application name.
	 * 
	 * @return
	 *         Apllication name
	 */
	@Override
	public String getApplication() {
		return this.applicationName;
	}

	/**
	 * Returns the timestamp.
	 * 
	 * @return
	 *         Timestamp
	 */
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Returns the value.
	 * 
	 * @return
	 *         Value
	 */
	@Override
	public double getValue() {
		return this.value;
	}

	/**
	 * Returns the forecast.
	 * 
	 * @return
	 *         Forecast
	 */
	@Override
	public double getForecast() {
		return this.forecast;
	}

	/**
	 * Returns the anomaly score.
	 * 
	 * @return
	 *         Anomaly score
	 */
	@Override
	public double getScore() {
		return this.score;
	}

	/**
	 * Returns the anomaly threshold.
	 * 
	 * @return
	 *         Anomaly threshold
	 */
	public double getAnomalyThreshold() {
		return this.anomalyThreshold;
	}
}
