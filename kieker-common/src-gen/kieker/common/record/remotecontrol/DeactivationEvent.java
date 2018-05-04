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
package kieker.common.record.remotecontrol;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.remotecontrol.IRemoteControlEvent;

/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.14
 */
public class DeactivationEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IRemoteControlEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING; // IRemoteControlEvent.pattern
	
	public static final Class<?>[] TYPES = {
		String.class, // IRemoteControlEvent.pattern
	};
	
	/** default constants. */
	public static final String PATTERN = "";
	private static final long serialVersionUID = 8080379087547198579L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"pattern",
	};
	
	/** property declarations. */
	private final String pattern;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 */
	public DeactivationEvent(final String pattern) {
		this.pattern = pattern == null?"":pattern;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	public DeactivationEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.pattern = (String) values[0];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected DeactivationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.pattern = (String) values[0];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public DeactivationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getPattern(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putString(this.getPattern());
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
	 * @deprecated to be rmeoved in 1.15
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
		
		final DeactivationEvent castedRecord = (DeactivationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getPattern().equals(castedRecord.getPattern())) {
			return false;
		}
		
		return true;
	}
	
	public final String getPattern() {
		return this.pattern;
	}
	
}
