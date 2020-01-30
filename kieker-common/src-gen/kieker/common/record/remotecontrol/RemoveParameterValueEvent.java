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
package kieker.common.record.remotecontrol;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.remotecontrol.IParameterValueEvent;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.15
 */
public class RemoveParameterValueEvent extends AbstractMonitoringRecord implements IParameterValueEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // IRemoteControlEvent.pattern
			 + TYPE_SIZE_STRING // IParameterValueEvent.name
			 + TYPE_SIZE_STRING; // IParameterValueEvent.value
	
	public static final Class<?>[] TYPES = {
		String.class, // IRemoteControlEvent.pattern
		String.class, // IParameterValueEvent.name
		String.class, // IParameterValueEvent.value
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"pattern",
		"name",
		"value",
	};
	
	/** default constants. */
	public static final String PATTERN = "";
	public static final String NAME = "";
	public static final String VALUE = "";
	private static final long serialVersionUID = 3932734275237128548L;
	
	/** property declarations. */
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public RemoveParameterValueEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
		this.name = deserializer.getString();
		this.value = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getPattern());
		serializer.putString(this.getName());
		serializer.putString(this.getValue());
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
		
		final RemoveParameterValueEvent castedRecord = (RemoveParameterValueEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getPattern().equals(castedRecord.getPattern())) {
			return false;
		}
		if (!this.getName().equals(castedRecord.getName())) {
			return false;
		}
		if (!this.getValue().equals(castedRecord.getValue())) {
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
		code += this.getValue().hashCode();
		
		return code;
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
