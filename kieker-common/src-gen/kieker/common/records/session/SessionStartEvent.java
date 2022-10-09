/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.records.session;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.flow.IEventRecord;
import kieker.common.records.session.ISessionEvent;

/**
 * @author Reiner Jung
 * API compatibility: Kieker 1.15.0
 * 
 * @since 2.0.0
 */
public class SessionStartEvent extends AbstractMonitoringRecord implements IEventRecord, ISessionEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_STRING // ISessionEvent.hostname
			 + TYPE_SIZE_STRING; // ISessionEvent.sessionId
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // ISessionEvent.hostname
		String.class, // ISessionEvent.sessionId
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"sessionId",
	};
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final String SESSION_ID = "";
	private static final long serialVersionUID = -2432476605280542594L;
	
	/** property declarations. */
	private long timestamp;
	private final String hostname;
	private final String sessionId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param sessionId
	 *            sessionId
	 */
	public SessionStartEvent(final long timestamp, final String hostname, final String sessionId) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?"":hostname;
		this.sessionId = sessionId == null?"":sessionId;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public SessionStartEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.sessionId = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getSessionId());
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
		
		final SessionStartEvent castedRecord = (SessionStartEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
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
		code += ((int)this.getTimestamp());
		code += this.getHostname().hashCode();
		code += this.getSessionId().hashCode();
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "SessionStartEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "sessionId = ";
		result += this.getSessionId() + ", ";
		
		return result;
	}
}
