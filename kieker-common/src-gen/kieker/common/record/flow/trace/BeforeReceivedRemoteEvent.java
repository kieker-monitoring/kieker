package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Felix Eichhorst
 * API compatibility: Kieker 1.10.0
 * 
 * @since 1.14
 */
public class BeforeReceivedRemoteEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = -2469910628320520231L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // BeforeReceivedRemoteEvent.timestamp
			 + TYPE_SIZE_LONG // BeforeReceivedRemoteEvent.callerTraceId
			 + TYPE_SIZE_INT // BeforeReceivedRemoteEvent.callerOrderIndex
			 + TYPE_SIZE_LONG // BeforeReceivedRemoteEvent.traceId
			 + TYPE_SIZE_INT // BeforeReceivedRemoteEvent.orderIndex
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // BeforeReceivedRemoteEvent.timestamp
		long.class, // BeforeReceivedRemoteEvent.callerTraceId
		int.class, // BeforeReceivedRemoteEvent.callerOrderIndex
		long.class, // BeforeReceivedRemoteEvent.traceId
		int.class, // BeforeReceivedRemoteEvent.orderIndex
	};
	
	
	/** default constants. */
	public static final long TIMESTAMP = -1L;
	public static final long CALLER_TRACE_ID = -1L;
	public static final int CALLER_ORDER_INDEX = -1;
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"callerTraceId",
		"callerOrderIndex",
		"traceId",
		"orderIndex",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final long callerTraceId;
	private final int callerOrderIndex;
	private final long traceId;
	private final int orderIndex;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param callerTraceId
	 *            callerTraceId
	 * @param callerOrderIndex
	 *            callerOrderIndex
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 */
	public BeforeReceivedRemoteEvent(final long timestamp, final long callerTraceId, final int callerOrderIndex, final long traceId, final int orderIndex) {
		this.timestamp = timestamp;
		this.callerTraceId = callerTraceId;
		this.callerOrderIndex = callerOrderIndex;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeReceivedRemoteEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public BeforeReceivedRemoteEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.callerTraceId = (Long) values[1];
		this.callerOrderIndex = (Integer) values[2];
		this.traceId = (Long) values[3];
		this.orderIndex = (Integer) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeReceivedRemoteEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected BeforeReceivedRemoteEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.callerTraceId = (Long) values[1];
		this.callerOrderIndex = (Integer) values[2];
		this.traceId = (Long) values[3];
		this.orderIndex = (Integer) values[4];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 *
	 * @deprecated since 1.13. Use {@link #BeforeReceivedRemoteEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public BeforeReceivedRemoteEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.callerTraceId = buffer.getLong();
		this.callerOrderIndex = buffer.getInt();
		this.traceId = buffer.getLong();
		this.orderIndex = buffer.getInt();
	}
	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public BeforeReceivedRemoteEvent(final IValueDeserializer deserializer) {
		this.timestamp = deserializer.getLong();
		this.callerTraceId = deserializer.getLong();
		this.callerOrderIndex = deserializer.getInt();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
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
			this.getTimestamp(),
			this.getCallerTraceId(),
			this.getCallerOrderIndex(),
			this.getTraceId(),
			this.getOrderIndex()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
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
		
		final BeforeReceivedRemoteEvent castedRecord = (BeforeReceivedRemoteEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getCallerTraceId() != castedRecord.getCallerTraceId()) return false;
		if (this.getCallerOrderIndex() != castedRecord.getCallerOrderIndex()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final long getCallerTraceId() {
		return this.callerTraceId;
	}
	
	
	public final int getCallerOrderIndex() {
		return this.callerOrderIndex;
	}
	
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}

	@Override
	public void serialize(IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getCallerTraceId());
		serializer.putInt(this.getCallerOrderIndex());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
	}
	
}
