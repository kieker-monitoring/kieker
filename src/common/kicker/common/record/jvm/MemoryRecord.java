/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.common.record.jvm;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kicker.common.record.AbstractMonitoringRecord;
import kicker.common.util.registry.IRegistry;

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

	private final long heapMax;
	private final long heapUsed;
	private final long heapCommitted;
	private final long heapInit;

	private final long nonHeapMax;
	private final long nonHeapUsed;
	private final long nonHeapCommitted;
	private final long nonHeapInit;

	private final int objectPendingFinalizationCount;

	public MemoryRecord(final long timestamp, final String hostname, final String vmName, final long heapMax, final long heapUsed, final long heapCommitted,
			final long heapInit, final long nonHeapMax, final long nonHeapUsed, final long nonHeapCommitted, final long nonHeapInit,
			final int objectPendingFinalizationCount) {
		super(timestamp, hostname, vmName);

		this.heapMax = heapMax;
		this.heapUsed = heapUsed;
		this.heapCommitted = heapCommitted;
		this.heapInit = heapInit;

		this.nonHeapMax = nonHeapMax;
		this.nonHeapUsed = nonHeapUsed;
		this.nonHeapCommitted = nonHeapCommitted;
		this.nonHeapInit = nonHeapInit;

		this.objectPendingFinalizationCount = objectPendingFinalizationCount;
	}

	public MemoryRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values);
		AbstractMonitoringRecord.checkArray(values, TYPES);

		this.heapMax = (Long) values[3];
		this.heapUsed = (Long) values[4];
		this.heapCommitted = (Long) values[5];
		this.heapInit = (Long) values[6];

		this.nonHeapMax = (Long) values[7];
		this.nonHeapUsed = (Long) values[8];
		this.nonHeapCommitted = (Long) values[9];
		this.nonHeapInit = (Long) values[10];

		this.objectPendingFinalizationCount = (Integer) values[11];
	}

	public MemoryRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);

		this.heapMax = buffer.getLong();
		this.heapUsed = buffer.getLong();
		this.heapCommitted = buffer.getLong();
		this.heapInit = buffer.getLong();

		this.nonHeapMax = buffer.getLong();
		this.nonHeapUsed = buffer.getLong();
		this.nonHeapCommitted = buffer.getLong();
		this.nonHeapInit = buffer.getLong();

		this.objectPendingFinalizationCount = buffer.getInt();
	}

	@Override
	public Object[] toArray() {
		return new Object[] { super.getTimestamp(), super.getHostname(), super.getVmName(), this.getHeapMax(), this.getHeapUsed(), this.getHeapCommitted(),
			this.getHeapInit(), this.getNonHeapMax(), this.getNonHeapUsed(), this.getNonHeapCommitted(), this.getNonHeapInit(),
			this.getObjectPendingFinalizationCount(), };
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		super.writeBytes(buffer, stringRegistry);

		buffer.putLong(this.getHeapMax());
		buffer.putLong(this.getHeapUsed());
		buffer.putLong(this.getHeapCommitted());
		buffer.putLong(this.getHeapInit());

		buffer.putLong(this.getNonHeapMax());
		buffer.putLong(this.getNonHeapUsed());
		buffer.putLong(this.getNonHeapCommitted());
		buffer.putLong(this.getNonHeapInit());

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

	public long getHeapMax() {
		return this.heapMax;
	}

	public long getHeapUsed() {
		return this.heapUsed;
	}

	public long getHeapCommitted() {
		return this.heapCommitted;
	}

	public long getHeapInit() {
		return this.heapInit;
	}

	public long getNonHeapMax() {
		return this.nonHeapMax;
	}

	public long getNonHeapUsed() {
		return this.nonHeapUsed;
	}

	public long getNonHeapCommitted() {
		return this.nonHeapCommitted;
	}

	public long getNonHeapInit() {
		return this.nonHeapInit;
	}

	public int getObjectPendingFinalizationCount() {
		return this.objectPendingFinalizationCount;
	}

}
