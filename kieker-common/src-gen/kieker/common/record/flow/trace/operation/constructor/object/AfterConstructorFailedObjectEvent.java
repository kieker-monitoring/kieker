package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.IObjectRecord;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class AfterConstructorFailedObjectEvent extends AfterConstructorFailedEvent implements IObjectRecord {
	private static final long serialVersionUID = -8160283153301963516L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_STRING // IExceptionRecord.cause
			 + TYPE_SIZE_INT // IObjectRecord.objectId
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // IExceptionRecord.cause
		int.class, // IObjectRecord.objectId
	};
	
	/** user-defined constants */
	
	/** default constants */
	public static final int OBJECT_ID = 0;
	
	/** property declarations */
	private final int objectId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param cause
	 *            cause
	 * @param objectId
	 *            objectId
	 */
	public AfterConstructorFailedObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String cause, final int objectId) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature, cause);
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public AfterConstructorFailedObjectEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.objectId = (Integer) values[6];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AfterConstructorFailedObjectEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.objectId = (Integer) values[6];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param deserializer
	 *            The deserializer to use
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AfterConstructorFailedObjectEvent(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(deserializer, buffer, stringRegistry);
		this.objectId = deserializer.getInt(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getCause(),
			this.getObjectId()
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
	public void writeBytes(final IValueSerializer serializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp(), buffer);
		serializer.putLong(this.getTraceId(), buffer);
		serializer.putInt(this.getOrderIndex(), buffer);
		serializer.putString(this.getOperationSignature(), buffer, stringRegistry);
		serializer.putString(this.getClassSignature(), buffer, stringRegistry);
		serializer.putString(this.getCause(), buffer, stringRegistry);
		serializer.putInt(this.getObjectId(), buffer);
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
	public void initFromBytes(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
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
		
		final AfterConstructorFailedObjectEvent castedRecord = (AfterConstructorFailedObjectEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (!this.getCause().equals(castedRecord.getCause())) {
			return false;
		}
		if (this.getObjectId() != castedRecord.getObjectId()) {
			return false;
		}
		return true;
	}
	
	@Override
	public final int getObjectId() {
		return this.objectId;
	}	
}
