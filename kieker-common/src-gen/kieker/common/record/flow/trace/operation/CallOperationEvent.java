package kieker.common.record.flow.trace.operation;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.ICallRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 * 
 * @since 1.5
 */
public class CallOperationEvent extends AbstractOperationEvent implements ICallRecord {
	private static final long serialVersionUID = 1340141343488227597L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_STRING // ICallRecord.calleeOperationSignature
			 + TYPE_SIZE_STRING // ICallRecord.calleeClassSignature
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // ICallRecord.calleeOperationSignature
		String.class, // ICallRecord.calleeClassSignature
	};
	
	/** user-defined constants */
	
	/** default constants */
	public static final String CALLEE_OPERATION_SIGNATURE = "";
	public static final String CALLEE_CLASS_SIGNATURE = "";
	
	/** property declarations */
	private final String calleeOperationSignature;
	private final String calleeClassSignature;
	
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
	 * @param calleeOperationSignature
	 *            calleeOperationSignature
	 * @param calleeClassSignature
	 *            calleeClassSignature
	 */
	public CallOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String calleeOperationSignature, final String calleeClassSignature) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.calleeOperationSignature = calleeOperationSignature == null?CALLEE_OPERATION_SIGNATURE:calleeOperationSignature;
		this.calleeClassSignature = calleeClassSignature == null?CALLEE_CLASS_SIGNATURE:calleeClassSignature;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public CallOperationEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.calleeOperationSignature = (String) values[5];
		this.calleeClassSignature = (String) values[6];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected CallOperationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.calleeOperationSignature = (String) values[5];
		this.calleeClassSignature = (String) values[6];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param deserializer
	 *            The value deserializer to use.
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public CallOperationEvent(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(deserializer, buffer, stringRegistry);
		
		this.calleeOperationSignature = deserializer.getString(buffer, stringRegistry);
		this.calleeClassSignature = deserializer.getString(buffer, stringRegistry);
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
			this.getCalleeOperationSignature(),
			this.getCalleeClassSignature()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getCalleeOperationSignature());
		stringRegistry.get(this.getCalleeClassSignature());
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
		serializer.putString(this.getCalleeOperationSignature(), buffer, stringRegistry);
		serializer.putString(this.getCalleeClassSignature(), buffer, stringRegistry);
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
		
		final CallOperationEvent castedRecord = (CallOperationEvent) obj;
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
		if (!this.getCalleeOperationSignature().equals(castedRecord.getCalleeOperationSignature())) {
			return false;
		}
		if (!this.getCalleeClassSignature().equals(castedRecord.getCalleeClassSignature())) {
			return false;
		}
		return true;
	}
	
	@Override
	public final String getCallerOperationSignature() {
		return this.getOperationSignature();
	}	
	
	@Override
	public final String getCallerClassSignature() {
		return this.getClassSignature();
	}	
	
	@Override
	public final String getCalleeOperationSignature() {
		return this.calleeOperationSignature;
	}	
	
	@Override
	public final String getCalleeClassSignature() {
		return this.calleeClassSignature;
	}	
}
