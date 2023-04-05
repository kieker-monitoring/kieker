/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Christian Zirkelbach
 * API compatibility: Kieker 2.0.0
 * 
 * @since 1.14
 */
public class ApplicationTraceMetadata extends TraceMetadata  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // TraceMetadata.traceId
			 + TYPE_SIZE_LONG // TraceMetadata.threadId
			 + TYPE_SIZE_STRING // TraceMetadata.sessionId
			 + TYPE_SIZE_STRING // TraceMetadata.hostname
			 + TYPE_SIZE_LONG // TraceMetadata.parentTraceId
			 + TYPE_SIZE_INT // TraceMetadata.parentOrderId
			 + TYPE_SIZE_STRING; // ApplicationTraceMetadata.applicationName
	
	public static final Class<?>[] TYPES = {
		long.class, // TraceMetadata.traceId
		long.class, // TraceMetadata.threadId
		String.class, // TraceMetadata.sessionId
		String.class, // TraceMetadata.hostname
		long.class, // TraceMetadata.parentTraceId
		int.class, // TraceMetadata.parentOrderId
		String.class, // ApplicationTraceMetadata.applicationName
	};
	
	/** user-defined constants. */
	public static final String NO_APPLICATION_NAME = "<no-application-name>";
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"traceId",
		"threadId",
		"sessionId",
		"hostname",
		"parentTraceId",
		"parentOrderId",
		"applicationName",
	};
	
	/** default constants. */
	public static final String APPLICATION_NAME = NO_APPLICATION_NAME;
	private static final long serialVersionUID = 7720995073835113293L;
	
	/** property declarations. */
	private final String applicationName;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            traceId
	 * @param threadId
	 *            threadId
	 * @param sessionId
	 *            sessionId
	 * @param hostname
	 *            hostname
	 * @param parentTraceId
	 *            parentTraceId
	 * @param parentOrderId
	 *            parentOrderId
	 * @param applicationName
	 *            applicationName
	 */
	public ApplicationTraceMetadata(final long traceId, final long threadId, final String sessionId, final String hostname, final long parentTraceId, final int parentOrderId, final String applicationName) {
		super(traceId, threadId, sessionId, hostname, parentTraceId, parentOrderId);
		this.applicationName = applicationName == null?NO_APPLICATION_NAME:applicationName;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ApplicationTraceMetadata(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.applicationName = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTraceId());
		serializer.putLong(this.getThreadId());
		serializer.putString(this.getSessionId());
		serializer.putString(this.getHostname());
		serializer.putLong(this.getParentTraceId());
		serializer.putInt(this.getParentOrderId());
		serializer.putString(this.getApplicationName());
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
		
		final ApplicationTraceMetadata castedRecord = (ApplicationTraceMetadata) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getThreadId() != castedRecord.getThreadId()) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (this.getParentTraceId() != castedRecord.getParentTraceId()) {
			return false;
		}
		if (this.getParentOrderId() != castedRecord.getParentOrderId()) {
			return false;
		}
		if (this.getNextOrderId() != castedRecord.getNextOrderId()) {
			return false;
		}
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) {
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
		code += ((int)this.getTraceId());
		code += ((int)this.getThreadId());
		code += this.getSessionId().hashCode();
		code += this.getHostname().hashCode();
		code += ((int)this.getParentTraceId());
		code += ((int)this.getParentOrderId());
		code += ((int)this.getNextOrderId());
		code += this.getApplicationName().hashCode();
		
		return code;
	}
	
	public final String getApplicationName() {
		return this.applicationName;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ApplicationTraceMetadata: ";
		result += "traceId = ";
		result += this.getTraceId() + ", ";
		
		result += "threadId = ";
		result += this.getThreadId() + ", ";
		
		result += "sessionId = ";
		result += this.getSessionId() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "parentTraceId = ";
		result += this.getParentTraceId() + ", ";
		
		result += "parentOrderId = ";
		result += this.getParentOrderId() + ", ";
		
		result += "applicationName = ";
		result += this.getApplicationName() + ", ";
		
		return result;
	}
}
