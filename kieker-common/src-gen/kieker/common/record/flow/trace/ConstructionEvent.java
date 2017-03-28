package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public class ConstructionEvent extends AbstractTraceEvent {
	private static final long serialVersionUID = 7724747240454611559L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_STRING // ConstructionEvent.classSignature
			+ TYPE_SIZE_INT // ConstructionEvent.objectId
	;

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // ConstructionEvent.classSignature
		int.class, // ConstructionEvent.objectId
	};

	/** user-defined constants */

	/** default constants */
	public static final String CLASS_SIGNATURE = "";
	public static final int OBJECT_ID = 0;

	/** property declarations */
	private final String classSignature;
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
	 * @param classSignature
	 *            classSignature
	 * @param objectId
	 *            objectId
	 */
	public ConstructionEvent(final long timestamp, final long traceId, final int orderIndex, final String classSignature, final int objectId) {
		super(timestamp, traceId, orderIndex);
		this.classSignature = classSignature == null ? CLASS_SIGNATURE : classSignature;
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 *
	 * @param values
	 *            The values for the record.
	 */
	public ConstructionEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ConstructionEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
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
	public ConstructionEvent(final IValueDeserializer deserializer) throws BufferUnderflowException {
		super(deserializer);

		this.classSignature = deserializer.getString();
		this.objectId = deserializer.getInt();
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
			this.getClassSignature(),
			this.getObjectId()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getClassSignature());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getClassSignature());
		serializer.putInt(this.getObjectId());
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

		final ConstructionEvent castedRecord = (ConstructionEvent) obj;
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
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (this.getObjectId() != castedRecord.getObjectId()) {
			return false;
		}
		return true;
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
