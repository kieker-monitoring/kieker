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
public class ClassLoadingRecord extends AbstractJVMRecord {

	public static final int SIZE = AbstractJVMRecord.SIZE + TYPE_SIZE_LONG + TYPE_SIZE_INT + TYPE_SIZE_LONG;

	public static final Class<?>[] TYPES = {
		long.class, // timestamp
		String.class, // hostname
		String.class, // vmName
		long.class,
		int.class,
		long.class,
	};

	private static final long serialVersionUID = 2989308154952301746L;

	private final long totalLoadedClassCount;
	private final int loadedClassCount;
	private final long unloadedClassCount;

	public ClassLoadingRecord(final long timestamp, final String hostname, final String vmName, final long totalLoadedClassCount, final int loadedClassCount,
			final long unloadedClassCount) {
		super(timestamp, hostname, vmName);

		this.totalLoadedClassCount = totalLoadedClassCount;
		this.loadedClassCount = loadedClassCount;
		this.unloadedClassCount = unloadedClassCount;
	}

	public ClassLoadingRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values);
		AbstractMonitoringRecord.checkArray(values, TYPES);

		this.totalLoadedClassCount = (Long) values[3];
		this.loadedClassCount = (Integer) values[4];
		this.unloadedClassCount = (Long) values[5];
	}

	public ClassLoadingRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);

		this.totalLoadedClassCount = buffer.getLong();
		this.loadedClassCount = buffer.getInt();
		this.unloadedClassCount = buffer.getLong();
	}

	@Override
	public Object[] toArray() {
		return new Object[] { super.getTimestamp(), super.getHostname(), super.getVmName(), this.getTotalLoadedClassCount(), this.getLoadedClassCount(),
			this.getUnloadedClassCount(), };
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		super.writeBytes(buffer, stringRegistry);

		buffer.putLong(this.getTotalLoadedClassCount());
		buffer.putInt(this.getLoadedClassCount());
		buffer.putLong(this.getUnloadedClassCount());
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

	public long getTotalLoadedClassCount() {
		return this.totalLoadedClassCount;
	}

	public int getLoadedClassCount() {
		return this.loadedClassCount;
	}

	public long getUnloadedClassCount() {
		return this.unloadedClassCount;
	}

}
