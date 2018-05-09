/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Thomas Duellmann
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.10
 */
public class AggregationWindow extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AggregationWindow.windowStart
			 + TYPE_SIZE_LONG; // AggregationWindow.windowEnd
	
	public static final Class<?>[] TYPES = {
		long.class, // AggregationWindow.windowStart
		long.class, // AggregationWindow.windowEnd
	};
	
	private static final long serialVersionUID = -6015104562956593414L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"windowStart",
		"windowEnd",
	};
	
	/** property declarations. */
	private final long windowStart;
	private final long windowEnd;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param windowStart
	 *            windowStart
	 * @param windowEnd
	 *            windowEnd
	 */
	public AggregationWindow(final long windowStart, final long windowEnd) {
		this.windowStart = windowStart;
		this.windowEnd = windowEnd;
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public AggregationWindow(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.windowStart = deserializer.getLong();
		this.windowEnd = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getWindowStart());
		serializer.putLong(this.getWindowEnd());
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
		
		final AggregationWindow castedRecord = (AggregationWindow) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getWindowStart() != castedRecord.getWindowStart()) {
			return false;
		}
		if (this.getWindowEnd() != castedRecord.getWindowEnd()) {
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
		code += ((int)this.getWindowStart());
		code += ((int)this.getWindowEnd());
		
		return code;
	}
	
	public final long getWindowStart() {
		return this.windowStart;
	}
	
	
	public final long getWindowEnd() {
		return this.windowEnd;
	}
	
}
