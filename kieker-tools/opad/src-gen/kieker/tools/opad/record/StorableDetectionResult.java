/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Tom Frotscher, Thomas Duellmann
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.10
 */
public class StorableDetectionResult extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // StorableDetectionResult.applicationName
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.value
			 + TYPE_SIZE_LONG // StorableDetectionResult.timestamp
			 + TYPE_SIZE_DOUBLE // StorableDetectionResult.forecast
			 + TYPE_SIZE_DOUBLE; // StorableDetectionResult.score
	
	public static final Class<?>[] TYPES = {
		String.class, // StorableDetectionResult.applicationName
		double.class, // StorableDetectionResult.value
		long.class, // StorableDetectionResult.timestamp
		double.class, // StorableDetectionResult.forecast
		double.class, // StorableDetectionResult.score
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"applicationName",
		"value",
		"timestamp",
		"forecast",
		"score",
	};
	
	/** default constants. */
	public static final String APPLICATION_NAME = "";
	private static final long serialVersionUID = -758350040827117227L;
	
	/** property declarations. */
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public StorableDetectionResult(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.applicationName = deserializer.getString();
		this.value = deserializer.getDouble();
		this.timestamp = deserializer.getLong();
		this.forecast = deserializer.getDouble();
		this.score = deserializer.getDouble();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getApplicationName());
		serializer.putDouble(this.getValue());
		serializer.putLong(this.getTimestamp());
		serializer.putDouble(this.getForecast());
		serializer.putDouble(this.getScore());
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
		return VALUE_NAMES; // NOPMD
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
		
		final StorableDetectionResult castedRecord = (StorableDetectionResult) obj;
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
		
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += this.getApplicationName().hashCode();
		code += ((int)this.getValue());
		code += ((int)this.getTimestamp());
		code += ((int)this.getForecast());
		code += ((int)this.getScore());
		
		return code;
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
