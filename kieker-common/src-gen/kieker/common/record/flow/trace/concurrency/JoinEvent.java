package kieker.common.record.flow.trace.concurrency;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public class JoinEvent extends AbstractTraceEvent  {
	private static final long serialVersionUID = -7303666475064047694L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_LONG // JoinEvent.joinedTraceId
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		long.class, // JoinEvent.joinedTraceId
	};
	
	/** user-defined constants */
	
	/** default constants */
	public static final long JOINED_TRACE_ID = 0L;
	
	/** property declarations */
	private final long joinedTraceId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param joinedTraceId
	 *            joinedTraceId
	 */
	public JoinEvent(final long timestamp, final long traceId, final int orderIndex, final long joinedTraceId) {
		super(timestamp, traceId, orderIndex);
		this.joinedTraceId = joinedTraceId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public JoinEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.joinedTraceId = (Long) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected JoinEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.joinedTraceId = (Long) values[3];
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
	public JoinEvent(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(deserializer, buffer, stringRegistry);
		this.joinedTraceId = deserializer.getLong(buffer);
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
			this.getJoinedTraceId()
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
	public void writeBytes(final IValueSerializer serializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp(), buffer);
		serializer.putLong(this.getTraceId(), buffer);
		serializer.putInt(this.getOrderIndex(), buffer);
		serializer.putLong(this.getJoinedTraceId(), buffer);
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
		
		final JoinEvent castedRecord = (JoinEvent) obj;
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
		if (this.getJoinedTraceId() != castedRecord.getJoinedTraceId()) {
			return false;
		}
		return true;
	}
	
	public final long getJoinedTraceId() {
		return this.joinedTraceId;
	}	
}
