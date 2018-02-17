/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Christian Zirkelbach
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.14
 */
public class ApplicationTraceMetadata extends TraceMetadata  {
	private static final long serialVersionUID = 7720995073835113293L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // TraceMetadata.traceId
			 + TYPE_SIZE_LONG // TraceMetadata.threadId
			 + TYPE_SIZE_STRING // TraceMetadata.sessionId
			 + TYPE_SIZE_STRING // TraceMetadata.hostname
			 + TYPE_SIZE_LONG // TraceMetadata.parentTraceId
			 + TYPE_SIZE_INT // TraceMetadata.parentOrderId
			 + TYPE_SIZE_STRING // ApplicationTraceMetadata.applicationName
	;
	
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
	
	/** default constants. */
	public static final String APPLICATION_NAME = NO_APPLICATION_NAME;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"traceId",
		"threadId",
		"sessionId",
		"hostname",
		"parentTraceId",
		"parentOrderId",
		"applicationName",
	};
	
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
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #ApplicationTraceMetadata(IValueDeserializer)} instead.
	 */
	@Deprecated
	public ApplicationTraceMetadata(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.applicationName = (String) values[7];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #ApplicationTraceMetadata(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ApplicationTraceMetadata(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.applicationName = (String) values[7];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public ApplicationTraceMetadata(final IValueDeserializer deserializer) {
		super(deserializer);
		this.applicationName = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTraceId(),
			this.getThreadId(),
			this.getSessionId(),
			this.getHostname(),
			this.getParentTraceId(),
			this.getParentOrderId(),
			this.getApplicationName()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getSessionId());
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getApplicationName());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
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
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
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
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final ApplicationTraceMetadata castedRecord = (ApplicationTraceMetadata) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		if (!this.getSessionId().equals(castedRecord.getSessionId())) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (this.getParentTraceId() != castedRecord.getParentTraceId()) return false;
		if (this.getParentOrderId() != castedRecord.getParentOrderId()) return false;
		if (this.getNextOrderId() != castedRecord.getNextOrderId()) return false;
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) return false;
		return true;
	}
	
	public final String getApplicationName() {
		return this.applicationName;
	}
	
}
