/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.remotecontrol.IRemoteControlEvent;

/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.14
 */
public class ActivationEvent extends AbstractMonitoringRecord implements IRemoteControlEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING; // IRemoteControlEvent.pattern
	
	public static final Class<?>[] TYPES = {
		String.class, // IRemoteControlEvent.pattern
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"pattern",
	};
	
	/** default constants. */
	public static final String PATTERN = "";
	private static final long serialVersionUID = 6888923121890229913L;
	
	/** property declarations. */
	private final String pattern;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 */
	public ActivationEvent(final String pattern) {
		this.pattern = pattern == null?"":pattern;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ActivationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
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
		
		final ActivationEvent castedRecord = (ActivationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getPattern().equals(castedRecord.getPattern())) {
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
		
		return code;
	}
	
	public final String getPattern() {
		return this.pattern;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ActivationEvent: ";
		result += "pattern = ";
		result += this.getPattern() + ", ";
		
		return result;
	}
}
