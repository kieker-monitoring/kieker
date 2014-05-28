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
public class CompilationRecord extends AbstractJVMRecord {

	public static final int SIZE = AbstractJVMRecord.SIZE + TYPE_SIZE_STRING + TYPE_SIZE_LONG;

	public static final Class<?>[] TYPES = {
		long.class, // timestamp
		String.class, // hostname
		String.class, // vmName
		String.class,
		long.class,
	};

	private static final long serialVersionUID = 2989308154952301746L;
	private final String jitCompilerName;
	private final long totalCompilationTime;

	public CompilationRecord(final long timestamp, final String hostname, final String vmName, final String jitCompilerName, final long totalCompilationTime) {
		super(timestamp, hostname, vmName);

		this.jitCompilerName = jitCompilerName;
		this.totalCompilationTime = totalCompilationTime;
	}

	public CompilationRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values);
		AbstractMonitoringRecord.checkArray(values, TYPES);

		this.jitCompilerName = (String) values[3];
		this.totalCompilationTime = (Long) values[4];
	}

	public CompilationRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);

		this.jitCompilerName = stringRegistry.get(buffer.getInt());
		this.totalCompilationTime = buffer.getLong();
	}

	@Override
	public Object[] toArray() {
		return new Object[] { super.getTimestamp(), super.getHostname(), super.getVmName(), this.getJitCompilerName(), this.getTotalCompilationTime() };
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		super.writeBytes(buffer, stringRegistry);

		buffer.putInt(stringRegistry.get(this.getJitCompilerName()));
		buffer.putLong(this.getTotalCompilationTime());
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

	public String getJitCompilerName() {
		return this.jitCompilerName;
	}

	public long getTotalCompilationTime() {
		return this.totalCompilationTime;
	}

}
