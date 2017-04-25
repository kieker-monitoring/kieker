/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.remotecontrol.IParameterValueEvent;

/**
 * @author Reiner Jung
 * 
 * @since 1.15
 */
public class RemoveParameterValueEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IParameterValueEvent {
	private static final long serialVersionUID = 3932734275237128548L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_STRING // IRemoteControlEvent.pattern
				 + TYPE_SIZE_STRING // IParameterValueEvent.name
				 + TYPE_SIZE_STRING // IParameterValueEvent.value
		;
	
		public static final Class<?>[] TYPES = {
			String.class, // IRemoteControlEvent.pattern
			String.class, // IParameterValueEvent.name
			String.class, // IParameterValueEvent.value
		};
	
	/** user-defined constants */

	/** default constants */
	public static final String PATTERN = "";
	public static final String NAME = "";
	public static final String VALUE = "";

	/** property declarations */
	private final String pattern;
	private final String name;
	private final String value;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 * @param name
	 *            name
	 * @param value
	 *            value
	 */
	public RemoveParameterValueEvent(final String pattern, final String name, final String value) {
		this.pattern = pattern == null?"":pattern;
		this.name = name == null?"":name;
		this.value = value == null?"":value;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public RemoveParameterValueEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.pattern = (String) values[0];
		this.name = (String) values[1];
		this.value = (String) values[2];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected RemoveParameterValueEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.pattern = (String) values[0];
		this.name = (String) values[1];
		this.value = (String) values[2];
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
	public RemoveParameterValueEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.pattern = stringRegistry.get(buffer.getInt());
		this.name = stringRegistry.get(buffer.getInt());
		this.value = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getPattern(),
			this.getName(),
			this.getValue()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getPattern());
		stringRegistry.get(this.getName());
		stringRegistry.get(this.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getPattern()));
		buffer.putInt(stringRegistry.get(this.getName()));
		buffer.putInt(stringRegistry.get(this.getValue()));
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
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final RemoveParameterValueEvent castedRecord = (RemoveParameterValueEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getPattern().equals(castedRecord.getPattern())) return false;
		if (!this.getName().equals(castedRecord.getName())) return false;
		if (!this.getValue().equals(castedRecord.getValue())) return false;
		return true;
	}
	
	public final String getPattern() {
		return this.pattern;
	}	
	
	public final String getName() {
		return this.name;
	}	
	
	public final String getValue() {
		return this.value;
	}	
}
