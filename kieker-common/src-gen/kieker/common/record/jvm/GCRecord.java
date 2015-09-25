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

package kieker.common.record.jvm;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import kieker.common.record.jvm.AbstractJVMRecord;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class GCRecord extends AbstractJVMRecord  {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_STRING // GCRecord.gcName
			 + TYPE_SIZE_LONG // GCRecord.collectionCount
			 + TYPE_SIZE_LONG // GCRecord.collectionTimeMS
	;
	private static final long serialVersionUID = 6474827601195369151L;
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		String.class, // GCRecord.gcName
		long.class, // GCRecord.collectionCount
		long.class, // GCRecord.collectionTimeMS
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String GC_NAME = "";
	/* property declarations */
	private final String gcName;
	private final long collectionCount;
	private final long collectionTimeMS;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param gcName
	 *            gcName
	 * @param collectionCount
	 *            collectionCount
	 * @param collectionTimeMS
	 *            collectionTimeMS
	 */
	public GCRecord(final long timestamp, final String hostname, final String vmName, final String gcName, final long collectionCount, final long collectionTimeMS) {
		super(timestamp, hostname, vmName);
		this.gcName = gcName == null?"":gcName;
		this.collectionCount = collectionCount;
		this.collectionTimeMS = collectionTimeMS;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public GCRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.gcName = (String) values[3];
		this.collectionCount = (Long) values[4];
		this.collectionTimeMS = (Long) values[5];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected GCRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.gcName = (String) values[3];
		this.collectionCount = (Long) values[4];
		this.collectionTimeMS = (Long) values[5];
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
	public GCRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.gcName = stringRegistry.get(buffer.getInt());
		this.collectionCount = buffer.getLong();
		this.collectionTimeMS = buffer.getLong();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getVmName(),
			this.getGcName(),
			this.getCollectionCount(),
			this.getCollectionTimeMS()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getVmName());
		stringRegistry.get(this.getGcName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getVmName()));
		buffer.putInt(stringRegistry.get(this.getGcName()));
		buffer.putLong(this.getCollectionCount());
		buffer.putLong(this.getCollectionTimeMS());
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
		final GCRecord castedRecord = (GCRecord) record;
		if (!this.gcName.equals(castedRecord.gcName)) return false;
		if (this.collectionCount != castedRecord.collectionCount) return false;
		if (this.collectionTimeMS != castedRecord.collectionTimeMS) return false;
		return super.equalsInternal(castedRecord);
	}

	public final String getGcName() {
		return this.gcName;
	}
	
	public final long getCollectionCount() {
		return this.collectionCount;
	}
	
	public final long getCollectionTimeMS() {
		return this.collectionTimeMS;
	}
	
}
