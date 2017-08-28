package kieker.common.record.flow.thread;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.flow.thread.AbstractThreadBasedEvent;
import kieker.common.record.io.*;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IExceptionRecord;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public class AfterFailedThreadBasedEvent extends AbstractThreadBasedEvent implements IExceptionRecord {
	private static final long serialVersionUID = -561668403011752179L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // IThreadBasedRecord.threadId
			 + TYPE_SIZE_INT // IThreadBasedRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_STRING // IExceptionRecord.cause
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // IThreadBasedRecord.threadId
		int.class, // IThreadBasedRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // IExceptionRecord.cause
	};
	
	/** user-defined constants */
	
	/** default constants */
	public static final String CAUSE = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"threadId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"cause",
	};
	
	/** property declarations */
	private final String cause;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param threadId
	 *            threadId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param cause
	 *            cause
	 */
	public AfterFailedThreadBasedEvent(final long timestamp, final long threadId, final int orderIndex, final String operationSignature, final String classSignature, final String cause) {
		super(timestamp, threadId, orderIndex, operationSignature, classSignature);
		this.cause = cause == null?CAUSE:cause;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public AfterFailedThreadBasedEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.cause = (String) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AfterFailedThreadBasedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.cause = (String) values[5];
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AfterFailedThreadBasedEvent(final IValueDeserializer deserializer) throws BufferUnderflowException {
		super(deserializer);
		this.cause = deserializer.getString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getThreadId(),
			this.getOrderIndex(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getCause()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getCause());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		super.serialize(serializer);
		serializer.putString(this.getCause());
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
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final AfterFailedThreadBasedEvent castedRecord = (AfterFailedThreadBasedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (!this.getCause().equals(castedRecord.getCause())) return false;
		return true;
	}
	
	public final String getCause() {
		return this.cause;
	}

	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES;	// NOPMD
	}	
}
