/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.common.record.system;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
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
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final String DEVICE_NAME = "";
	public static final double QUEUE = 0.0;
	public static final double READ_BYTES_PER_SECOND = 0.0;
	public static final double READS_PER_SECOND = 0.0;
	public static final double SERVICE_TIME = 0.0;
	public static final double WRITE_BYTES_PER_SECOND = 0.0;
	public static final double WRITES_PER_SECOND = 0.0;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"deviceName",
		"queue",
		"readBytesPerSecond",
		"readsPerSecond",
		"serviceTime",
		"writeBytesPerSecond",
		"writesPerSecond",
	};
	
	/** property declarations. */
	private long timestamp;
	private String hostname;
	private String deviceName;
	private double queue;
	private double readBytesPerSecond;
	private double readsPerSecond;
	private double serviceTime;
	private double writeBytesPerSecond;
	private double writesPerSecond;
	
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
	public DiskUsageRecord(final long timestamp, final String hostname, final String deviceName, final double queue, final double readBytesPerSecond,
			final double readsPerSecond, final double serviceTime, final double writeBytesPerSecond, final double writesPerSecond) {
		this.timestamp = timestamp;
		this.hostname = hostname == null ? HOSTNAME : hostname;
		this.deviceName = deviceName == null ? DEVICE_NAME : deviceName;
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
	 * @param deserializer
	 *            The deserializer to use
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public DiskUsageRecord(final IValueDeserializer deserializer) throws BufferUnderflowException {
		this.timestamp = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.deviceName = deserializer.getString();
		this.queue = deserializer.getDouble();
		this.readBytesPerSecond = deserializer.getDouble();
		this.readsPerSecond = deserializer.getDouble();
		this.serviceTime = deserializer.getDouble();
		this.writeBytesPerSecond = deserializer.getDouble();
		this.writesPerSecond = deserializer.getDouble();
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
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getDeviceName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getDeviceName());
		serializer.putDouble(this.getQueue());
		serializer.putDouble(this.getReadBytesPerSecond());
		serializer.putDouble(this.getReadsPerSecond());
		serializer.putDouble(this.getServiceTime());
		serializer.putDouble(this.getWriteBytesPerSecond());
		serializer.putDouble(this.getWritesPerSecond());
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
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final DiskUsageRecord castedRecord = (DiskUsageRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getDeviceName().equals(castedRecord.getDeviceName())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getQueue(), castedRecord.getQueue())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getReadBytesPerSecond(), castedRecord.getReadBytesPerSecond())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getReadsPerSecond(), castedRecord.getReadsPerSecond())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getServiceTime(), castedRecord.getServiceTime())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getWriteBytesPerSecond(), castedRecord.getWriteBytesPerSecond())) {
			return false;
		}
		if (AbstractMonitoringRecord.isNotEqual(this.getWritesPerSecond(), castedRecord.getWritesPerSecond())) {
			return false;
		}
		return true;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
	public final void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public final String getDeviceName() {
		return this.deviceName;
	}
	
	public final void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public final double getQueue() {
		return this.queue;
	}
	
	public final void setQueue(double queue) {
		this.queue = queue;
	}
	
	public final double getReadBytesPerSecond() {
		return this.readBytesPerSecond;
	}
	
	public final void setReadBytesPerSecond(double readBytesPerSecond) {
		this.readBytesPerSecond = readBytesPerSecond;
	}
	
	public final double getReadsPerSecond() {
		return this.readsPerSecond;
	}
	
	public final void setReadsPerSecond(double readsPerSecond) {
		this.readsPerSecond = readsPerSecond;
	}
	
	public final double getServiceTime() {
		return this.serviceTime;
	}
	
	public final void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public final double getWriteBytesPerSecond() {
		return this.writeBytesPerSecond;
	}
	
	public final void setWriteBytesPerSecond(double writeBytesPerSecond) {
		this.writeBytesPerSecond = writeBytesPerSecond;
	}
	
	public final double getWritesPerSecond() {
		return this.writesPerSecond;
	}
	
	public final void setWritesPerSecond(double writesPerSecond) {
		this.writesPerSecond = writesPerSecond;
	}

}
