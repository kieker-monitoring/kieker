package kieker.common.record.flow.trace.operation.object;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.flow.ICallObjectRecord;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public class CallOperationObjectEvent extends CallOperationEvent implements ICallObjectRecord {
	private static final long serialVersionUID = 357965549135860700L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_STRING // IOperationSignature.operationSignature
			+ TYPE_SIZE_STRING // IClassSignature.classSignature
			+ TYPE_SIZE_STRING // ICallRecord.calleeOperationSignature
			+ TYPE_SIZE_STRING // ICallRecord.calleeClassSignature
			+ TYPE_SIZE_INT // IObjectRecord.objectId
			+ TYPE_SIZE_INT // ICallObjectRecord.calleeObjectId
	;

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // ICallRecord.calleeOperationSignature
		String.class, // ICallRecord.calleeClassSignature
		int.class, // IObjectRecord.objectId
		int.class, // ICallObjectRecord.calleeObjectId
	};

	/** user-defined constants */

	/** default constants */
	public static final int OBJECT_ID = 0;
	public static final int CALLEE_OBJECT_ID = 0;

	/** property declarations */
	private final int objectId;
	private final int calleeObjectId;

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
	 * @param objectId
	 *            objectId
	 * @param calleeObjectId
	 *            calleeObjectId
	 */
	public CallOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature,
			final String calleeOperationSignature, final String calleeClassSignature, final int objectId, final int calleeObjectId) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature, calleeOperationSignature, calleeClassSignature);
		this.objectId = objectId;
		this.calleeObjectId = calleeObjectId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 *
	 * @param values
	 *            The values for the record.
	 */
	public CallOperationObjectEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.objectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected CallOperationObjectEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.objectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	/**
	 * This constructor converts the given array into a record.
	 *
	 * @param deserializer
	 *            The deserializer to use
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public CallOperationObjectEvent(final IValueDeserializer deserializer) throws BufferUnderflowException {
		super(deserializer);

		this.objectId = deserializer.getInt();
		this.calleeObjectId = deserializer.getInt();
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
			this.getCalleeClassSignature(),
			this.getObjectId(),
			this.getCalleeObjectId()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getCalleeOperationSignature());
		stringRegistry.get(this.getCalleeClassSignature());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getCalleeOperationSignature());
		serializer.putString(this.getCalleeClassSignature());
		serializer.putInt(this.getObjectId());
		serializer.putInt(this.getCalleeObjectId());
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
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final CallOperationObjectEvent castedRecord = (CallOperationObjectEvent) obj;
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
		if (this.getObjectId() != castedRecord.getObjectId()) {
			return false;
		}
		if (this.getCalleeObjectId() != castedRecord.getCalleeObjectId()) {
			return false;
		}
		return true;
	}

	@Override
	public final int getObjectId() {
		return this.objectId;
	}

	@Override
	public final int getCallerObjectId() {
		return this.getObjectId();
	}

	@Override
	public final int getCalleeObjectId() {
		return this.calleeObjectId;
	}
}
