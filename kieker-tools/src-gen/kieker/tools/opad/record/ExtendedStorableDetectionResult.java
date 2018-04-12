/***************************************************************************
 * Copyright 2018 iObserve Project (https://iobserve-devops.net)
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

import kieker.common.exception.RecordInstantiationException;
import kieker.tools.opad.record.StorableDetectionResult;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.registry.IRegistry;


/**
 * @author Thomas Duellmann
 * API compatibility: Kieker 1.14.0
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
			 + TYPE_SIZE_DOUBLE; // ExtendedStorableDetectionResult.anomalyThreshold
	
	public static final Class<?>[] TYPES = {
		String.class, // StorableDetectionResult.applicationName
		double.class, // StorableDetectionResult.value
		long.class, // StorableDetectionResult.timestamp
		double.class, // StorableDetectionResult.forecast
		double.class, // StorableDetectionResult.score
		double.class, // ExtendedStorableDetectionResult.anomalyThreshold
	};
	
	private static final long serialVersionUID = 3489846495430494003L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"applicationName",
		"value",
		"timestamp",
		"forecast",
		"score",
		"anomalyThreshold",
	};
	
	/** property declarations. */
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
	 *
	 * @deprecated since 1.13. Use {@link #ExtendedStorableDetectionResult(IValueDeserializer)} instead.
	 */
	@Deprecated
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
	 *
	 * @deprecated since 1.13. Use {@link #ExtendedStorableDetectionResult(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ExtendedStorableDetectionResult(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.anomalyThreshold = (Double) values[5];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ExtendedStorableDetectionResult(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.anomalyThreshold = deserializer.getDouble();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getApplicationName(),
			this.getValue(),
			this.getTimestamp(),
			this.getForecast(),
			this.getScore(),
			this.getAnomalyThreshold(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putString(this.getApplicationName());
		serializer.putDouble(this.getValue());
		serializer.putLong(this.getTimestamp());
		serializer.putDouble(this.getForecast());
		serializer.putDouble(this.getScore());
		serializer.putDouble(this.getAnomalyThreshold());
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
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
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
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final ExtendedStorableDetectionResult castedRecord = (ExtendedStorableDetectionResult) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) {
			return false;
		}
		if (isNotEqual(this.getValue(), castedRecord.getValue())) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (isNotEqual(this.getForecast(), castedRecord.getForecast())) {
			return false;
		}
		if (isNotEqual(this.getScore(), castedRecord.getScore())) {
			return false;
		}
		if (isNotEqual(this.getAnomalyThreshold(), castedRecord.getAnomalyThreshold())) {
			return false;
		}
		
		return true;
	}
	
	public final double getAnomalyThreshold() {
		return this.anomalyThreshold;
	}
	
}
