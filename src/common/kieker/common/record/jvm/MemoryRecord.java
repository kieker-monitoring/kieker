/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class MemoryRecord extends AbstractJVMRecord {

	public static final int SIZE = AbstractJVMRecord.SIZE + (8 * TYPE_SIZE_LONG) + TYPE_SIZE_INT;

	public static final Class<?>[] TYPES = {
		long.class, // timestamp
		String.class, // hostname
		String.class, // vmName
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
		int.class,
	};

	private static final long serialVersionUID = 2989308154952301746L;

	private final long heapMaxBytes;
	private final long heapUsedBytes;
	private final long heapCommittedBytes;
	private final long heapInitBytes;

	private final long nonHeapMaxBytes;
	private final long nonHeapUsedBytes;
	private final long nonHeapCommittedBytes;
	private final long nonHeapInitBytes;

	private final int objectPendingFinalizationCount;

	public MemoryRecord(final long timestamp, final String hostname, final String vmName, final long heapMaxBytes, final long heapUsedBytes,
			final long heapCommittedBytes, final long heapInitBytes, final long nonHeapMaxBytes, final long nonHeapUsedBytes, final long nonHeapCommittedBytes,
			final long nonHeapInitBytes, final int objectPendingFinalizationCount) {
		super(timestamp, hostname, vmName);

		this.heapMaxBytes = heapMaxBytes;
		this.heapUsedBytes = heapUsedBytes;
		this.heapCommittedBytes = heapCommittedBytes;
		this.heapInitBytes = heapInitBytes;

		this.nonHeapMaxBytes = nonHeapMaxBytes;
		this.nonHeapUsedBytes = nonHeapUsedBytes;
		this.nonHeapCommittedBytes = nonHeapCommittedBytes;
		this.nonHeapInitBytes = nonHeapInitBytes;

		this.objectPendingFinalizationCount = objectPendingFinalizationCount;
	}

	public MemoryRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values);
		AbstractMonitoringRecord.checkArray(values, TYPES);

		this.heapMaxBytes = (Long) values[3];
		this.heapUsedBytes = (Long) values[4];
		this.heapCommittedBytes = (Long) values[5];
		this.heapInitBytes = (Long) values[6];

		this.nonHeapMaxBytes = (Long) values[7];
		this.nonHeapUsedBytes = (Long) values[8];
		this.nonHeapCommittedBytes = (Long) values[9];
		this.nonHeapInitBytes = (Long) values[10];

		this.objectPendingFinalizationCount = (Integer) values[11];
	}

	public MemoryRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);

		this.heapMaxBytes = buffer.getLong();
		this.heapUsedBytes = buffer.getLong();
		this.heapCommittedBytes = buffer.getLong();
		this.heapInitBytes = buffer.getLong();

		this.nonHeapMaxBytes = buffer.getLong();
		this.nonHeapUsedBytes = buffer.getLong();
		this.nonHeapCommittedBytes = buffer.getLong();
		this.nonHeapInitBytes = buffer.getLong();

		this.objectPendingFinalizationCount = buffer.getInt();
	}

	@Override
	public Object[] toArray() {
		return new Object[] { super.getTimestamp(), super.getHostname(), super.getVmName(), this.getHeapMaxBytes(), this.getHeapUsedBytes(),
			this.getHeapCommittedBytes(), this.getHeapInitBytes(), this.getNonHeapMaxBytes(), this.getNonHeapUsedBytes(), this.getNonHeapCommittedBytes(),
			this.getNonHeapInitBytes(), this.getObjectPendingFinalizationCount(), };
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		super.writeBytes(buffer, stringRegistry);

		buffer.putLong(this.getHeapMaxBytes());
		buffer.putLong(this.getHeapUsedBytes());
		buffer.putLong(this.getHeapCommittedBytes());
		buffer.putLong(this.getHeapInitBytes());

		buffer.putLong(this.getNonHeapMaxBytes());
		buffer.putLong(this.getNonHeapUsedBytes());
		buffer.putLong(this.getNonHeapCommittedBytes());
		buffer.putLong(this.getNonHeapInitBytes());

		buffer.putInt(this.getObjectPendingFinalizationCount());
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

	public long getHeapMaxBytes() {
		return this.heapMaxBytes;
	}

	public long getHeapUsedBytes() {
		return this.heapUsedBytes;
	}

	public long getHeapCommittedBytes() {
		return this.heapCommittedBytes;
	}

	public long getHeapInitBytes() {
		return this.heapInitBytes;
	}

	public long getNonHeapMaxBytes() {
		return this.nonHeapMaxBytes;
	}

	public long getNonHeapUsedBytes() {
		return this.nonHeapUsedBytes;
	}

	public long getNonHeapCommittedBytes() {
		return this.nonHeapCommittedBytes;
	}

	public long getNonHeapInitBytes() {
		return this.nonHeapInitBytes;
	}

	public int getObjectPendingFinalizationCount() {
		return this.objectPendingFinalizationCount;
	}

}
