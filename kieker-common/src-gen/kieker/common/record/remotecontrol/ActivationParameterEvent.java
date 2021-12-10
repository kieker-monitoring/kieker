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

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;

/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.14
 */
public class ActivationParameterEvent extends AbstractMonitoringRecord implements IRemoteParameterControlEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // IRemoteControlEvent.pattern
			 + TYPE_SIZE_STRING // IRemoteParameterControlEvent.name
			 + TYPE_SIZE_STRING; // IRemoteParameterControlEvent.values
	
	public static final Class<?>[] TYPES = {
		String.class, // IRemoteControlEvent.pattern
		String.class, // IRemoteParameterControlEvent.name
		String[].class, // IRemoteParameterControlEvent.values
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"pattern",
		"name",
		"values",
	};
	
	/** default constants. */
	public static final String PATTERN = "";
	public static final String NAME = "";
	private static final long serialVersionUID = 4522862409472272138L;
	
	/** property declarations. */
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
	public ActivationParameterEvent(final String pattern, final String name, final String[] values) {
		this.pattern = pattern == null?"":pattern;
		this.name = name == null?"":name;
		this.values = values;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ActivationParameterEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
		this.name = deserializer.getString();
		// load array sizes
		final int _values_size0 = deserializer.getInt();
		this.values = new String[_values_size0];
		for (int i0=0;i0<_values_size0;i0++)
			this.values[i0] = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getPattern());
		serializer.putString(this.getName());
		// store array sizes
		int _values_size0 = this.getValues().length;
		serializer.putInt(_values_size0);
		for (int i0=0;i0<_values_size0;i0++)
			serializer.putString(this.getValues()[i0]);
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
		
		final ActivationParameterEvent castedRecord = (ActivationParameterEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getPattern().equals(castedRecord.getPattern())) {
			return false;
		}
		if (!this.getName().equals(castedRecord.getName())) {
			return false;
		}
		// get array length
		int _values_size0 = this.getValues().length;
		if (_values_size0 != castedRecord.getValues().length) {
			return false;
		}
		for (int i0=0;i0<_values_size0;i0++)
			if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) {
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
		code += this.getPattern().hashCode();
		code += this.getName().hashCode();
		// get array length
		for (int i0=0;i0 < this.values.length;i0++) {
			for (int i1=0;i1 < this.values.length;i1++) {
				code += this.getValues()[i0].hashCode();
			}
		}
		
		return code;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ActivationParameterEvent: ";
		result += "pattern = ";
		result += this.getPattern() + ", ";
		
		result += "name = ";
		result += this.getName() + ", ";
		
		result += "values = ";
		// store array sizes
		int _values_size0 = this.getValues().length;
		result += "{ ";
		for (int i0=0;i0<_values_size0;i0++)
			result += this.getValues()[i0] + ", ";
		result += " }";
		
		return result;
	}
}
