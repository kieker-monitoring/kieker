package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public abstract class AbstractMonitorEvent extends AbstractTraceEvent  {
	private static final long serialVersionUID = 8385865083415561635L;

	
	/** user-defined constants */
	
	/** default constants */
	public static final int LOCK_ID = 0;
	
	/** property declarations */
	private final int lockId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param lockId
	 *            lockId
	 */
	public AbstractMonitorEvent(final long timestamp, final long traceId, final int orderIndex, final int lockId) {
		super(timestamp, traceId, orderIndex);
		this.lockId = lockId;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractMonitorEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.lockId = (Integer) values[3];
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
	public AbstractMonitorEvent(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(deserializer, buffer, stringRegistry);
		this.lockId = buffer.getInt();
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
		
		final AbstractMonitorEvent castedRecord = (AbstractMonitorEvent) obj;
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
		if (this.getLockId() != castedRecord.getLockId()) {
			return false;
		}
		return true;
	}
	
	public final int getLockId() {
		return this.lockId;
	}	
}
