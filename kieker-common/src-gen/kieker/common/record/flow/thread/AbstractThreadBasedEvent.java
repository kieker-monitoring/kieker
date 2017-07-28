package kieker.common.record.flow.thread;

import java.nio.*;

import kieker.common.record.flow.AbstractEvent;
import kieker.common.record.flow.IThreadBasedRecord;
import kieker.common.record.io.*;
import kieker.common.record.flow.IOperationSignature;
import kieker.common.record.flow.IClassSignature;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public abstract class AbstractThreadBasedEvent extends AbstractEvent implements IThreadBasedRecord, IOperationSignature, IClassSignature {
	private static final long serialVersionUID = -4756765331556509113L;

	
	/** user-defined constants */
	
	/** default constants */
	public static final long THREAD_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";
	
	/** property declarations */
	private final long threadId;
	private final int orderIndex;
	private final String operationSignature;
	private final String classSignature;
	
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
	 */
	public AbstractThreadBasedEvent(final long timestamp, final long threadId, final int orderIndex, final String operationSignature, final String classSignature) {
		super(timestamp);
		this.threadId = threadId;
		this.orderIndex = orderIndex;
		this.operationSignature = operationSignature == null?OPERATION_SIGNATURE:operationSignature;
		this.classSignature = classSignature == null?CLASS_SIGNATURE:classSignature;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractThreadBasedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.threadId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
		this.operationSignature = (String) values[3];
		this.classSignature = (String) values[4];
	}

	/**
	 * This constructor converts the given array into a record.
	 *
	 * @param deserializer
	 *            The value deserializer to use.
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AbstractThreadBasedEvent(final IValueDeserializer deserializer) throws BufferUnderflowException {
		super(deserializer);
		this.threadId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.operationSignature = deserializer.getString();
		this.classSignature = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
//		super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getThreadId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
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
		
		final AbstractThreadBasedEvent castedRecord = (AbstractThreadBasedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		return true;
	}
	
	public final long getThreadId() {
		return this.threadId;
	}	
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}	
	
	public final String getOperationSignature() {
		return this.operationSignature;
	}	
	
	public final String getClassSignature() {
		return this.classSignature;
	}	
}
