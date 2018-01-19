/***************************************************************************
* Copyright 2018 Kieker Project (http://www.iobserve-devops.net)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
***************************************************************************/
package kieker.common.record.system;

import java.nio.BufferOverflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Andre van Hoorn, Jan Waller
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.3
 */
public class MemSwapUsageRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 638480390439299363L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // MemSwapUsageRecord.timestamp
			 + TYPE_SIZE_STRING // MemSwapUsageRecord.hostname
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.memTotal
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.memUsed
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.memFree
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.swapTotal
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.swapUsed
			 + TYPE_SIZE_LONG // MemSwapUsageRecord.swapFree
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // MemSwapUsageRecord.timestamp
		String.class, // MemSwapUsageRecord.hostname
		long.class, // MemSwapUsageRecord.memTotal
		long.class, // MemSwapUsageRecord.memUsed
		long.class, // MemSwapUsageRecord.memFree
		long.class, // MemSwapUsageRecord.swapTotal
		long.class, // MemSwapUsageRecord.swapUsed
		long.class, // MemSwapUsageRecord.swapFree
	};
	
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final long MEM_TOTAL = 0L;
	public static final long MEM_USED = 0L;
	public static final long MEM_FREE = 0L;
	public static final long SWAP_TOTAL = 0L;
	public static final long SWAP_USED = 0L;
	public static final long SWAP_FREE = 0L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"memTotal",
		"memUsed",
		"memFree",
		"swapTotal",
		"swapUsed",
		"swapFree",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final String hostname;
	private final long memTotal;
	private final long memUsed;
	private final long memFree;
	private final long swapTotal;
	private final long swapUsed;
	private final long swapFree;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param memTotal
	 *            memTotal
	 * @param memUsed
	 *            memUsed
	 * @param memFree
	 *            memFree
	 * @param swapTotal
	 *            swapTotal
	 * @param swapUsed
	 *            swapUsed
	 * @param swapFree
	 *            swapFree
	 */
	public MemSwapUsageRecord(final long timestamp, final String hostname, final long memTotal, final long memUsed, final long memFree, final long swapTotal, final long swapUsed, final long swapFree) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?HOSTNAME:hostname;
		this.memTotal = memTotal;
		this.memUsed = memUsed;
		this.memFree = memFree;
		this.swapTotal = swapTotal;
		this.swapUsed = swapUsed;
		this.swapFree = swapFree;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #MemSwapUsageRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	public MemSwapUsageRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.memTotal = (Long) values[2];
		this.memUsed = (Long) values[3];
		this.memFree = (Long) values[4];
		this.swapTotal = (Long) values[5];
		this.swapUsed = (Long) values[6];
		this.swapFree = (Long) values[7];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #MemSwapUsageRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected MemSwapUsageRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.memTotal = (Long) values[2];
		this.memUsed = (Long) values[3];
		this.memFree = (Long) values[4];
		this.swapTotal = (Long) values[5];
		this.swapUsed = (Long) values[6];
		this.swapFree = (Long) values[7];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public MemSwapUsageRecord(final IValueDeserializer deserializer) {
		this.timestamp = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.memTotal = deserializer.getLong();
		this.memUsed = deserializer.getLong();
		this.memFree = deserializer.getLong();
		this.swapTotal = deserializer.getLong();
		this.swapUsed = deserializer.getLong();
		this.swapFree = deserializer.getLong();
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
			this.getHostname(),
			this.getMemTotal(),
			this.getMemUsed(),
			this.getMemFree(),
			this.getSwapTotal(),
			this.getSwapUsed(),
			this.getSwapFree()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putLong(this.getMemTotal());
		serializer.putLong(this.getMemUsed());
		serializer.putLong(this.getMemFree());
		serializer.putLong(this.getSwapTotal());
		serializer.putLong(this.getSwapUsed());
		serializer.putLong(this.getSwapFree());
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
		
		final MemSwapUsageRecord castedRecord = (MemSwapUsageRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (this.getMemTotal() != castedRecord.getMemTotal()) return false;
		if (this.getMemUsed() != castedRecord.getMemUsed()) return false;
		if (this.getMemFree() != castedRecord.getMemFree()) return false;
		if (this.getSwapTotal() != castedRecord.getSwapTotal()) return false;
		if (this.getSwapUsed() != castedRecord.getSwapUsed()) return false;
		if (this.getSwapFree() != castedRecord.getSwapFree()) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final long getMemTotal() {
		return this.memTotal;
	}
	
	
	public final long getMemUsed() {
		return this.memUsed;
	}
	
	
	public final long getMemFree() {
		return this.memFree;
	}
	
	
	public final long getSwapTotal() {
		return this.swapTotal;
	}
	
	
	public final long getSwapUsed() {
		return this.swapUsed;
	}
	
	
	public final long getSwapFree() {
		return this.swapFree;
	}
	
}
