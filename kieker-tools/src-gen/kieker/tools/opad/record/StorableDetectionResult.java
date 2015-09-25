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

package kieker.tools.opad.record;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;


/**
 * @author Tom Frotscher, Thomas Duellmann
 * 
 * @since 1.10
 */
public class StorableDetectionResult extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // StorableDetectionResult.applicationName
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.value
			 + TYPE_SIZE_LONG // StorableDetectionResult.timestamp
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.forecast
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.score
	;
	private static final long serialVersionUID = 6603400678579037205L;
	
	public static final Class<?>[] TYPES = {
		String.class, // StorableDetectionResult.applicationName
		double.class, // StorableDetectionResult.value
		long.class, // StorableDetectionResult.timestamp
		double.class, // StorableDetectionResult.forecast
		double.class, // StorableDetectionResult.score
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String APPLICATION_NAME = "";
	/* property declarations */
	private final String applicationName;
	private final double value;
	private final long timestamp;
	private final double forecast;
	private final double score;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param applicationName
	 *            applicationName
	 * @param value
	 *            value
	 * @param timestamp
	 *            timestamp
	 * @param forecast
	 *            forecast
	 * @param score
	 *            score
	 */
	public StorableDetectionResult(final String applicationName, final double value, final long timestamp, final double forecast, final double score) {
		this.applicationName = applicationName == null?"":applicationName;
		this.value = value;
		this.timestamp = timestamp;
		this.forecast = forecast;
		this.score = score;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public StorableDetectionResult(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.applicationName = (String) values[0];
		this.value = (Double) values[1];
		this.timestamp = (Long) values[2];
		this.forecast = (Double) values[3];
		this.score = (Double) values[4];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected StorableDetectionResult(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.applicationName = (String) values[0];
		this.value = (Double) values[1];
		this.timestamp = (Long) values[2];
		this.forecast = (Double) values[3];
		this.score = (Double) values[4];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public StorableDetectionResult(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.applicationName = stringRegistry.get(buffer.getInt());
		this.value = buffer.getDouble();
		this.timestamp = buffer.getLong();
		this.forecast = buffer.getDouble();
		this.score = buffer.getDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getApplicationName(),
			this.getValue(),
			this.getTimestamp(),
			this.getForecast(),
			this.getScore()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getApplicationName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getApplicationName()));
		buffer.putDouble(this.getValue());
		buffer.putLong(this.getTimestamp());
		buffer.putDouble(this.getForecast());
		buffer.putDouble(this.getScore());
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
	 */
	@Override
	public int getSize() {
		return SIZE;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean equalsInternal(final kieker.common.record.IMonitoringRecord record) {
		final StorableDetectionResult castedRecord = (StorableDetectionResult) record;
		if (!this.applicationName.equals(castedRecord.applicationName)) return false;
		if (this.value != castedRecord.value) return false;
		if (this.timestamp != castedRecord.timestamp) return false;
		if (this.forecast != castedRecord.forecast) return false;
		if (this.score != castedRecord.score) return false;
		return super.equalsInternal(castedRecord);
	}

	public final String getApplicationName() {
		return this.applicationName;
	}
	
	public final double getValue() {
		return this.value;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final double getForecast() {
		return this.forecast;
	}
	
	public final double getScore() {
		return this.score;
	}
	
}
