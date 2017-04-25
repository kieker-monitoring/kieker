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

import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;

/**
 * @author Marc Adolf
 * 
 * @since 1.14
 */
public class UpdateParameterEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IRemoteParameterControlEvent {
	private static final long serialVersionUID = -6620482545947972483L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_STRING // IRemoteControlEvent.pattern
				 + TYPE_SIZE_STRING // IRemoteParameterControlEvent.name
				 + TYPE_SIZE_STRING // IRemoteParameterControlEvent.values
		;
	
		public static final Class<?>[] TYPES = {
			String.class, // IRemoteControlEvent.pattern
			String.class, // IRemoteParameterControlEvent.name
			String[].class, // IRemoteParameterControlEvent.values
		};
	
	/** user-defined constants */

	/** default constants */
	public static final String PATTERN = "";
	public static final String NAME = "";

	/** property declarations */
	private final String pattern;
	private final String name;
	private final String[] values;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 * @param name
	 *            name
	 * @param values
	 *            values
	 */
	public UpdateParameterEvent(final String pattern, final String name, final String[] values) {
		this.pattern = pattern == null?"":pattern;
		this.name = name == null?"":name;
		this.values = values;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public UpdateParameterEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.pattern = (String) values[0];
		this.name = (String) values[1];
		this.values = (String[]) values[2];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected UpdateParameterEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.pattern = (String) values[0];
		this.name = (String) values[1];
		this.values = (String[]) values[2];
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
	public UpdateParameterEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.pattern = stringRegistry.get(buffer.getInt());
		this.name = stringRegistry.get(buffer.getInt());
		// load array sizes
		int _values_size0 = buffer.getInt();
		this.values = new String[_values_size0];
		for (int i0=0;i0<_values_size0;i0++)
			this.values[i0] = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getPattern(),
			this.getName(),
			this.getValues()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getPattern());
		stringRegistry.get(this.getName());
		// get array length
		int _values_size0 = this.getValues().length;
		for (int i0=0;i0<_values_size0;i0++)
			stringRegistry.get(this.getValues()[i0]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getPattern()));
		buffer.putInt(stringRegistry.get(this.getName()));
		// store array sizes
		int _values_size0 = this.getValues().length;
		buffer.putInt(_values_size0);
		for (int i0=0;i0<_values_size0;i0++)
			buffer.putInt(stringRegistry.get(this.getValues()[i0]));
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
		
		final UpdateParameterEvent castedRecord = (UpdateParameterEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getPattern().equals(castedRecord.getPattern())) return false;
		if (!this.getName().equals(castedRecord.getName())) return false;
		// get array length
		int _values_size0 = this.getValues().length;
		if (_values_size0 != castedRecord.getValues().length)
			return false;
		for (int i0=0;i0<_values_size0;i0++)
			if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) return false;
		return true;
	}
	
	public final String getPattern() {
		return this.pattern;
	}	
	
	public final String getName() {
		return this.name;
	}	
	
	public final String[] getValues() {
		return this.values;
	}	
}
