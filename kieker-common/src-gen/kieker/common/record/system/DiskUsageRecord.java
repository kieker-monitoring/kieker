package kieker.common.record.system;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public class DiskUsageRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 2474236414042988334L;

		/** Descriptive definition of the serialization size of the record. */
		public static final int SIZE = TYPE_SIZE_LONG // DiskUsageRecord.timestamp
				 + TYPE_SIZE_STRING // DiskUsageRecord.hostname
				 + TYPE_SIZE_STRING // DiskUsageRecord.deviceName
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.queue
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.readBytesPerSecond
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.readsPerSecond
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.serviceTime
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.writeBytesPerSecond
				 + TYPE_SIZE_DOUBLE // DiskUsageRecord.writesPerSecond
		;
	
		public static final Class<?>[] TYPES = {
			long.class, // DiskUsageRecord.timestamp
			String.class, // DiskUsageRecord.hostname
			String.class, // DiskUsageRecord.deviceName
			double.class, // DiskUsageRecord.queue
			double.class, // DiskUsageRecord.readBytesPerSecond
			double.class, // DiskUsageRecord.readsPerSecond
			double.class, // DiskUsageRecord.serviceTime
			double.class, // DiskUsageRecord.writeBytesPerSecond
			double.class, // DiskUsageRecord.writesPerSecond
		};
	
	/** user-defined constants */

	/** default constants */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final String DEVICE_NAME = "";
	public static final double QUEUE = 0.0;
	public static final double READ_BYTES_PER_SECOND = 0.0;
	public static final double READS_PER_SECOND = 0.0;
	public static final double SERVICE_TIME = 0.0;
	public static final double WRITE_BYTES_PER_SECOND = 0.0;
	public static final double WRITES_PER_SECOND = 0.0;

	/** property declarations */
	private final long timestamp;
	private final String hostname;
	private final String deviceName;
	private final double queue;
	private final double readBytesPerSecond;
	private final double readsPerSecond;
	private final double serviceTime;
	private final double writeBytesPerSecond;
	private final double writesPerSecond;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param deviceName
	 *            deviceName
	 * @param queue
	 *            queue
	 * @param readBytesPerSecond
	 *            readBytesPerSecond
	 * @param readsPerSecond
	 *            readsPerSecond
	 * @param serviceTime
	 *            serviceTime
	 * @param writeBytesPerSecond
	 *            writeBytesPerSecond
	 * @param writesPerSecond
	 *            writesPerSecond
	 */
	public DiskUsageRecord(final long timestamp, final String hostname, final String deviceName, final double queue, final double readBytesPerSecond, final double readsPerSecond, final double serviceTime, final double writeBytesPerSecond, final double writesPerSecond) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?HOSTNAME:hostname;
		this.deviceName = deviceName == null?DEVICE_NAME:deviceName;
		this.queue = queue;
		this.readBytesPerSecond = readBytesPerSecond;
		this.readsPerSecond = readsPerSecond;
		this.serviceTime = serviceTime;
		this.writeBytesPerSecond = writeBytesPerSecond;
		this.writesPerSecond = writesPerSecond;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public DiskUsageRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.deviceName = (String) values[2];
		this.queue = (Double) values[3];
		this.readBytesPerSecond = (Double) values[4];
		this.readsPerSecond = (Double) values[5];
		this.serviceTime = (Double) values[6];
		this.writeBytesPerSecond = (Double) values[7];
		this.writesPerSecond = (Double) values[8];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected DiskUsageRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.deviceName = (String) values[2];
		this.queue = (Double) values[3];
		this.readBytesPerSecond = (Double) values[4];
		this.readsPerSecond = (Double) values[5];
		this.serviceTime = (Double) values[6];
		this.writeBytesPerSecond = (Double) values[7];
		this.writesPerSecond = (Double) values[8];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public DiskUsageRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.hostname = stringRegistry.get(buffer.getInt());
		this.deviceName = stringRegistry.get(buffer.getInt());
		this.queue = buffer.getDouble();
		this.readBytesPerSecond = buffer.getDouble();
		this.readsPerSecond = buffer.getDouble();
		this.serviceTime = buffer.getDouble();
		this.writeBytesPerSecond = buffer.getDouble();
		this.writesPerSecond = buffer.getDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getDeviceName(),
			this.getQueue(),
			this.getReadBytesPerSecond(),
			this.getReadsPerSecond(),
			this.getServiceTime(),
			this.getWriteBytesPerSecond(),
			this.getWritesPerSecond()
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getDeviceName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getDeviceName()));
		buffer.putDouble(this.getQueue());
		buffer.putDouble(this.getReadBytesPerSecond());
		buffer.putDouble(this.getReadsPerSecond());
		buffer.putDouble(this.getServiceTime());
		buffer.putDouble(this.getWriteBytesPerSecond());
		buffer.putDouble(this.getWritesPerSecond());
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
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
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
		
		final DiskUsageRecord castedRecord = (DiskUsageRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getDeviceName().equals(castedRecord.getDeviceName())) return false;
		if (isNotEqual(this.getQueue(), castedRecord.getQueue())) return false;
		if (isNotEqual(this.getReadBytesPerSecond(), castedRecord.getReadBytesPerSecond())) return false;
		if (isNotEqual(this.getReadsPerSecond(), castedRecord.getReadsPerSecond())) return false;
		if (isNotEqual(this.getServiceTime(), castedRecord.getServiceTime())) return false;
		if (isNotEqual(this.getWriteBytesPerSecond(), castedRecord.getWriteBytesPerSecond())) return false;
		if (isNotEqual(this.getWritesPerSecond(), castedRecord.getWritesPerSecond())) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}	
	
	public final String getHostname() {
		return this.hostname;
	}	
	
	public final String getDeviceName() {
		return this.deviceName;
	}	
	
	public final double getQueue() {
		return this.queue;
	}	
	
	public final double getReadBytesPerSecond() {
		return this.readBytesPerSecond;
	}	
	
	public final double getReadsPerSecond() {
		return this.readsPerSecond;
	}	
	
	public final double getServiceTime() {
		return this.serviceTime;
	}	
	
	public final double getWriteBytesPerSecond() {
		return this.writeBytesPerSecond;
	}	
	
	public final double getWritesPerSecond() {
		return this.writesPerSecond;
	}	
}
