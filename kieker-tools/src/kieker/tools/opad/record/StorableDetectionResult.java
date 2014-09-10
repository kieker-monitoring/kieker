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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * This class contains the data that will be stored in the database after each complete analysis. Therefore, containing the value, the application name, the forecast
 * calculated from the value, the timestamp and the corresponding anomaly score.
 * 
 * @author Tom Frotscher, Thomas Düllmann
 * @since 1.10
 */
public class StorableDetectionResult extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {

	public static final int SIZE = TYPE_SIZE_STRING + TYPE_SIZE_DOUBLE + TYPE_SIZE_LONG + TYPE_SIZE_DOUBLE + TYPE_SIZE_DOUBLE;
	public static final Class<?>[] TYPES = {
		String.class, // applicationName
		double.class, // value
		long.class, // timestamp
		double.class, // forecast
		double.class, // score
	};

	private static final long serialVersionUID = 7325786584057491433L;

	/**
	 * Name of the application.
	 */
	protected final String applicationName;

	/**
	 * Latency of the application.
	 */
	protected final double value;

	/**
	 * Timestamp of the record.
	 */
	protected final long timestamp;

	/**
	 * Forecast value.
	 */
	protected final double forecast;

	/**
	 * Anomaly score.
	 */
	protected final double score;

	public StorableDetectionResult(final String app, final double val, final long timest, final double fore, final double sc) {
		this.applicationName = app;
		this.value = val;
		this.timestamp = timest;
		this.forecast = fore;
		this.score = sc;
	}

	public StorableDetectionResult(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.applicationName = stringRegistry.get(buffer.getInt());
		this.value = buffer.getDouble();
		this.timestamp = buffer.getLong();
		this.forecast = buffer.getDouble();
		this.score = buffer.getDouble();
	}

	/**
	 * Creates an Instance of this class based on a single object array.
	 * 
	 * @param values
	 *            Object array containing the application name, value, timestamp, forecast and anomaly score.
	 */
	public StorableDetectionResult(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, StorableDetectionResult.TYPES);

		this.applicationName = (String) values[0];
		this.value = (Double) values[1];
		this.timestamp = (Long) values[2];
		this.forecast = (Double) values[3];
		this.score = (Double) values[4];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return StorableDetectionResult.TYPES.clone();
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

	public String getApplication() {
		return this.applicationName;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public double getValue() {
		return this.value;
	}

	public double getForecast() {
		return this.forecast;
	}

	public double getScore() {
		return this.score;
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getApplication()));
		buffer.putDouble(this.getValue());
		buffer.putLong(this.timestamp);
		buffer.putDouble(this.getForecast());
		buffer.putDouble(this.getScore());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSize() {
		return SIZE;
	}
}
