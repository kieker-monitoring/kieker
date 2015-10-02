/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;


/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public class NetworkUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // NetworkUtilizationRecord.timestamp
			 + TYPE_SIZE_STRING // NetworkUtilizationRecord.hostname
			 + TYPE_SIZE_STRING // NetworkUtilizationRecord.interfaceName
			 + TYPE_SIZE_LONG // NetworkUtilizationRecord.speed
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txBytesPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txCarrierPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txCollisionsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txDroppedPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txErrorsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txOverrunsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.txPacketsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxBytesPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxDroppedPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxErrorsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxFramePerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxOverrunsPerSecond
			 + TYPE_SIZE_DOUBLE // NetworkUtilizationRecord.rxPacketsPerSecond
	;
	private static final long serialVersionUID = 3706934665472860339L;
	
	public static final Class<?>[] TYPES = {
		long.class, // NetworkUtilizationRecord.timestamp
		String.class, // NetworkUtilizationRecord.hostname
		String.class, // NetworkUtilizationRecord.interfaceName
		long.class, // NetworkUtilizationRecord.speed
		double.class, // NetworkUtilizationRecord.txBytesPerSecond
		double.class, // NetworkUtilizationRecord.txCarrierPerSecond
		double.class, // NetworkUtilizationRecord.txCollisionsPerSecond
		double.class, // NetworkUtilizationRecord.txDroppedPerSecond
		double.class, // NetworkUtilizationRecord.txErrorsPerSecond
		double.class, // NetworkUtilizationRecord.txOverrunsPerSecond
		double.class, // NetworkUtilizationRecord.txPacketsPerSecond
		double.class, // NetworkUtilizationRecord.rxBytesPerSecond
		double.class, // NetworkUtilizationRecord.rxDroppedPerSecond
		double.class, // NetworkUtilizationRecord.rxErrorsPerSecond
		double.class, // NetworkUtilizationRecord.rxFramePerSecond
		double.class, // NetworkUtilizationRecord.rxOverrunsPerSecond
		double.class, // NetworkUtilizationRecord.rxPacketsPerSecond
	};
	
	/* user-defined constants */
	/* default constants */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final String INTERFACE_NAME = "";
	public static final long SPEED = 0L;
	public static final double TX_BYTES_PER_SECOND = 0.0;
	public static final double TX_CARRIER_PER_SECOND = 0.0;
	public static final double TX_COLLISIONS_PER_SECOND = 0.0;
	public static final double TX_DROPPED_PER_SECOND = 0.0;
	public static final double TX_ERRORS_PER_SECOND = 0.0;
	public static final double TX_OVERRUNS_PER_SECOND = 0.0;
	public static final double TX_PACKETS_PER_SECOND = 0.0;
	public static final double RX_BYTES_PER_SECOND = 0.0;
	public static final double RX_DROPPED_PER_SECOND = 0.0;
	public static final double RX_ERRORS_PER_SECOND = 0.0;
	public static final double RX_FRAME_PER_SECOND = 0.0;
	public static final double RX_OVERRUNS_PER_SECOND = 0.0;
	public static final double RX_PACKETS_PER_SECOND = 0.0;
	/* property declarations */
	private final long timestamp;
	private final String hostname;
	private final String interfaceName;
	private final long speed;
	private final double txBytesPerSecond;
	private final double txCarrierPerSecond;
	private final double txCollisionsPerSecond;
	private final double txDroppedPerSecond;
	private final double txErrorsPerSecond;
	private final double txOverrunsPerSecond;
	private final double txPacketsPerSecond;
	private final double rxBytesPerSecond;
	private final double rxDroppedPerSecond;
	private final double rxErrorsPerSecond;
	private final double rxFramePerSecond;
	private final double rxOverrunsPerSecond;
	private final double rxPacketsPerSecond;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param interfaceName
	 *            interfaceName
	 * @param speed
	 *            speed
	 * @param txBytesPerSecond
	 *            txBytesPerSecond
	 * @param txCarrierPerSecond
	 *            txCarrierPerSecond
	 * @param txCollisionsPerSecond
	 *            txCollisionsPerSecond
	 * @param txDroppedPerSecond
	 *            txDroppedPerSecond
	 * @param txErrorsPerSecond
	 *            txErrorsPerSecond
	 * @param txOverrunsPerSecond
	 *            txOverrunsPerSecond
	 * @param txPacketsPerSecond
	 *            txPacketsPerSecond
	 * @param rxBytesPerSecond
	 *            rxBytesPerSecond
	 * @param rxDroppedPerSecond
	 *            rxDroppedPerSecond
	 * @param rxErrorsPerSecond
	 *            rxErrorsPerSecond
	 * @param rxFramePerSecond
	 *            rxFramePerSecond
	 * @param rxOverrunsPerSecond
	 *            rxOverrunsPerSecond
	 * @param rxPacketsPerSecond
	 *            rxPacketsPerSecond
	 */
	public NetworkUtilizationRecord(final long timestamp, final String hostname, final String interfaceName, final long speed, final double txBytesPerSecond, final double txCarrierPerSecond, final double txCollisionsPerSecond, final double txDroppedPerSecond, final double txErrorsPerSecond, final double txOverrunsPerSecond, final double txPacketsPerSecond, final double rxBytesPerSecond, final double rxDroppedPerSecond, final double rxErrorsPerSecond, final double rxFramePerSecond, final double rxOverrunsPerSecond, final double rxPacketsPerSecond) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?HOSTNAME:hostname;
		this.interfaceName = interfaceName == null?INTERFACE_NAME:interfaceName;
		this.speed = speed;
		this.txBytesPerSecond = txBytesPerSecond;
		this.txCarrierPerSecond = txCarrierPerSecond;
		this.txCollisionsPerSecond = txCollisionsPerSecond;
		this.txDroppedPerSecond = txDroppedPerSecond;
		this.txErrorsPerSecond = txErrorsPerSecond;
		this.txOverrunsPerSecond = txOverrunsPerSecond;
		this.txPacketsPerSecond = txPacketsPerSecond;
		this.rxBytesPerSecond = rxBytesPerSecond;
		this.rxDroppedPerSecond = rxDroppedPerSecond;
		this.rxErrorsPerSecond = rxErrorsPerSecond;
		this.rxFramePerSecond = rxFramePerSecond;
		this.rxOverrunsPerSecond = rxOverrunsPerSecond;
		this.rxPacketsPerSecond = rxPacketsPerSecond;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public NetworkUtilizationRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.interfaceName = (String) values[2];
		this.speed = (Long) values[3];
		this.txBytesPerSecond = (Double) values[4];
		this.txCarrierPerSecond = (Double) values[5];
		this.txCollisionsPerSecond = (Double) values[6];
		this.txDroppedPerSecond = (Double) values[7];
		this.txErrorsPerSecond = (Double) values[8];
		this.txOverrunsPerSecond = (Double) values[9];
		this.txPacketsPerSecond = (Double) values[10];
		this.rxBytesPerSecond = (Double) values[11];
		this.rxDroppedPerSecond = (Double) values[12];
		this.rxErrorsPerSecond = (Double) values[13];
		this.rxFramePerSecond = (Double) values[14];
		this.rxOverrunsPerSecond = (Double) values[15];
		this.rxPacketsPerSecond = (Double) values[16];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected NetworkUtilizationRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.interfaceName = (String) values[2];
		this.speed = (Long) values[3];
		this.txBytesPerSecond = (Double) values[4];
		this.txCarrierPerSecond = (Double) values[5];
		this.txCollisionsPerSecond = (Double) values[6];
		this.txDroppedPerSecond = (Double) values[7];
		this.txErrorsPerSecond = (Double) values[8];
		this.txOverrunsPerSecond = (Double) values[9];
		this.txPacketsPerSecond = (Double) values[10];
		this.rxBytesPerSecond = (Double) values[11];
		this.rxDroppedPerSecond = (Double) values[12];
		this.rxErrorsPerSecond = (Double) values[13];
		this.rxFramePerSecond = (Double) values[14];
		this.rxOverrunsPerSecond = (Double) values[15];
		this.rxPacketsPerSecond = (Double) values[16];
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
	public NetworkUtilizationRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.hostname = stringRegistry.get(buffer.getInt());
		this.interfaceName = stringRegistry.get(buffer.getInt());
		this.speed = buffer.getLong();
		this.txBytesPerSecond = buffer.getDouble();
		this.txCarrierPerSecond = buffer.getDouble();
		this.txCollisionsPerSecond = buffer.getDouble();
		this.txDroppedPerSecond = buffer.getDouble();
		this.txErrorsPerSecond = buffer.getDouble();
		this.txOverrunsPerSecond = buffer.getDouble();
		this.txPacketsPerSecond = buffer.getDouble();
		this.rxBytesPerSecond = buffer.getDouble();
		this.rxDroppedPerSecond = buffer.getDouble();
		this.rxErrorsPerSecond = buffer.getDouble();
		this.rxFramePerSecond = buffer.getDouble();
		this.rxOverrunsPerSecond = buffer.getDouble();
		this.rxPacketsPerSecond = buffer.getDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getInterfaceName(),
			this.getSpeed(),
			this.getTxBytesPerSecond(),
			this.getTxCarrierPerSecond(),
			this.getTxCollisionsPerSecond(),
			this.getTxDroppedPerSecond(),
			this.getTxErrorsPerSecond(),
			this.getTxOverrunsPerSecond(),
			this.getTxPacketsPerSecond(),
			this.getRxBytesPerSecond(),
			this.getRxDroppedPerSecond(),
			this.getRxErrorsPerSecond(),
			this.getRxFramePerSecond(),
			this.getRxOverrunsPerSecond(),
			this.getRxPacketsPerSecond()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getInterfaceName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getInterfaceName()));
		buffer.putLong(this.getSpeed());
		buffer.putDouble(this.getTxBytesPerSecond());
		buffer.putDouble(this.getTxCarrierPerSecond());
		buffer.putDouble(this.getTxCollisionsPerSecond());
		buffer.putDouble(this.getTxDroppedPerSecond());
		buffer.putDouble(this.getTxErrorsPerSecond());
		buffer.putDouble(this.getTxOverrunsPerSecond());
		buffer.putDouble(this.getTxPacketsPerSecond());
		buffer.putDouble(this.getRxBytesPerSecond());
		buffer.putDouble(this.getRxDroppedPerSecond());
		buffer.putDouble(this.getRxErrorsPerSecond());
		buffer.putDouble(this.getRxFramePerSecond());
		buffer.putDouble(this.getRxOverrunsPerSecond());
		buffer.putDouble(this.getRxPacketsPerSecond());
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
	protected boolean equalsInternal(final kieker.common.record.IMonitoringRecord record) {
		final NetworkUtilizationRecord castedRecord = (NetworkUtilizationRecord) record;
		if (this.timestamp != castedRecord.timestamp) return false;
		if (!this.hostname.equals(castedRecord.hostname)) return false;
		if (!this.interfaceName.equals(castedRecord.interfaceName)) return false;
		if (this.speed != castedRecord.speed) return false;
		if (isEqual(this.txBytesPerSecond, castedRecord.txBytesPerSecond)) return false;
		if (isEqual(this.txCarrierPerSecond, castedRecord.txCarrierPerSecond)) return false;
		if (isEqual(this.txCollisionsPerSecond, castedRecord.txCollisionsPerSecond)) return false;
		if (isEqual(this.txDroppedPerSecond, castedRecord.txDroppedPerSecond)) return false;
		if (isEqual(this.txErrorsPerSecond, castedRecord.txErrorsPerSecond)) return false;
		if (isEqual(this.txOverrunsPerSecond, castedRecord.txOverrunsPerSecond)) return false;
		if (isEqual(this.txPacketsPerSecond, castedRecord.txPacketsPerSecond)) return false;
		if (isEqual(this.rxBytesPerSecond, castedRecord.rxBytesPerSecond)) return false;
		if (isEqual(this.rxDroppedPerSecond, castedRecord.rxDroppedPerSecond)) return false;
		if (isEqual(this.rxErrorsPerSecond, castedRecord.rxErrorsPerSecond)) return false;
		if (isEqual(this.rxFramePerSecond, castedRecord.rxFramePerSecond)) return false;
		if (isEqual(this.rxOverrunsPerSecond, castedRecord.rxOverrunsPerSecond)) return false;
		if (isEqual(this.rxPacketsPerSecond, castedRecord.rxPacketsPerSecond)) return false;
		return super.equalsInternal(castedRecord);
	}

	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
	public final String getInterfaceName() {
		return this.interfaceName;
	}
	
	public final long getSpeed() {
		return this.speed;
	}
	
	public final double getTxBytesPerSecond() {
		return this.txBytesPerSecond;
	}
	
	public final double getTxCarrierPerSecond() {
		return this.txCarrierPerSecond;
	}
	
	public final double getTxCollisionsPerSecond() {
		return this.txCollisionsPerSecond;
	}
	
	public final double getTxDroppedPerSecond() {
		return this.txDroppedPerSecond;
	}
	
	public final double getTxErrorsPerSecond() {
		return this.txErrorsPerSecond;
	}
	
	public final double getTxOverrunsPerSecond() {
		return this.txOverrunsPerSecond;
	}
	
	public final double getTxPacketsPerSecond() {
		return this.txPacketsPerSecond;
	}
	
	public final double getRxBytesPerSecond() {
		return this.rxBytesPerSecond;
	}
	
	public final double getRxDroppedPerSecond() {
		return this.rxDroppedPerSecond;
	}
	
	public final double getRxErrorsPerSecond() {
		return this.rxErrorsPerSecond;
	}
	
	public final double getRxFramePerSecond() {
		return this.rxFramePerSecond;
	}
	
	public final double getRxOverrunsPerSecond() {
		return this.rxOverrunsPerSecond;
	}
	
	public final double getRxPacketsPerSecond() {
		return this.rxPacketsPerSecond;
	}
	
}
