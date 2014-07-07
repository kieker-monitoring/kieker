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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * Extended StorableDetectionResult class that extends the StorableDetectionResult class by anomaly threshold.
 * 
 * @author Thomas Düllmann
 * @since 1.10
 * 
 */
public class ExtendedStorableDetectionResult extends StorableDetectionResult {

	public static final int SIZE = StorableDetectionResult.SIZE + TYPE_SIZE_DOUBLE;

	public static final Class<?>[] TYPES = {
		String.class, // applicationName
		double.class, // value
		long.class, // timestamp
		double.class, // forecast
		double.class, // score
		double.class,
	};

	private static final long serialVersionUID = -5529586414782099554L;
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
		super(Arrays.copyOfRange(values, 0, values.length - 1));
		AbstractMonitoringRecord.checkArray(values, ExtendedStorableDetectionResult.TYPES);
		this.anomalyThreshold = (Double) values[5];
	}

	public ExtendedStorableDetectionResult(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.anomalyThreshold = buffer.getDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
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
		return new Object[] { super.getApplication(), super.getValue(), super.getTimestamp(), super.getForecast(), super.getScore(), this.getAnomalyThreshold() };
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

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		super.writeBytes(buffer, stringRegistry);
		buffer.putDouble(this.anomalyThreshold);
	}
}
