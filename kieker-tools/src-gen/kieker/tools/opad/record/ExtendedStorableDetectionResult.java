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

import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import kieker.tools.opad.record.StorableDetectionResult;

/**
 * @author Thomas Duellmann
 * 
 * @since 1.10
 */
public class ExtendedStorableDetectionResult extends StorableDetectionResult  {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // StorableDetectionResult.applicationName
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.value
			 + TYPE_SIZE_LONG // StorableDetectionResult.timestamp
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.forecast
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.score
			 + TYPE_SIZE_DOUBLE // ExtendedStorableDetectionResult.anomalyThreshold
	;
	private static final long serialVersionUID = 5201527358214171119L;
	
	public static final Class<?>[] TYPES = {
		String.class, // StorableDetectionResult.applicationName
		double.class, // StorableDetectionResult.value
		long.class, // StorableDetectionResult.timestamp
		double.class, // StorableDetectionResult.forecast
		double.class, // StorableDetectionResult.score
		double.class, // ExtendedStorableDetectionResult.anomalyThreshold
	};
	
	/* user-defined constants */
	/* default constants */
	/* property declarations */
	private final double anomalyThreshold;

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
	 * @param anomalyThreshold
	 *            anomalyThreshold
	 */
	public ExtendedStorableDetectionResult(final String applicationName, final double value, final long timestamp, final double forecast, final double score, final double anomalyThreshold) {
		super(applicationName, value, timestamp, forecast, score);
		this.anomalyThreshold = anomalyThreshold;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ExtendedStorableDetectionResult(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.anomalyThreshold = (Double) values[5];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ExtendedStorableDetectionResult(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.anomalyThreshold = (Double) values[5];
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
	public ExtendedStorableDetectionResult(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.anomalyThreshold = buffer.getDouble();
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
			this.getScore(),
			this.getAnomalyThreshold()
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
		buffer.putDouble(this.getAnomalyThreshold());
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
		final ExtendedStorableDetectionResult castedRecord = (ExtendedStorableDetectionResult) record;
		if (this.anomalyThreshold != castedRecord.anomalyThreshold) return false;
		return super.equalsInternal(castedRecord);
	}

	public final double getAnomalyThreshold() {
		return this.anomalyThreshold;
	}
	
}
