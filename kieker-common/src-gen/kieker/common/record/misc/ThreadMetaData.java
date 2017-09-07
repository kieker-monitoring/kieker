package kieker.common.record.misc;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public class ThreadMetaData extends AbstractMonitoringRecord
		implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 3010881635494258512L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // ThreadMetaData.hostName
			+ TYPE_SIZE_LONG // ThreadMetaData.threadId
	;

	public static final Class<?>[] TYPES = { String.class, // ThreadMetaData.hostName
			long.class, // ThreadMetaData.threadId
	};

	/** user-defined constants */

	/** default constants */
	public static final String HOST_NAME = "";

	/** property name array. */
	private static final String[] PROPERTY_NAMES = { "hostName", "threadId", };

	/** property declarations */
	private final String hostName;
	private final long threadId;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param hostName
	 *            hostName
	 * @param threadId
	 *            threadId
	 */
	public ThreadMetaData(final String hostName, final long threadId) {
		this.hostName = hostName == null ? "" : hostName;
		this.threadId = threadId;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result
	 * of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ThreadMetaData(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.hostName = (String) values[0];
		this.threadId = (Long) values[1];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ThreadMetaData(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.hostName = (String) values[0];
		this.threadId = (Long) values[1];
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public ThreadMetaData(final IValueDeserializer deserializer) throws BufferUnderflowException {
		this.hostName = deserializer.getString();
		this.threadId = deserializer.getLong();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.getHostName(), this.getThreadId() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getHostName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getHostName());
		serializer.putLong(this.getThreadId());
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
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this
	 *             method is not implemented.
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

		final ThreadMetaData castedRecord = (ThreadMetaData) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getHostName().equals(castedRecord.getHostName())) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		return true;
	}

	public final String getHostName() {
		return this.hostName;
	}

	public final long getThreadId() {
		return this.threadId;
	}

	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
	}
}
